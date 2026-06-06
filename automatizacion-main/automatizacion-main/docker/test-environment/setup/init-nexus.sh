#!/bin/bash
# ═══════════════════════════════════════════════════════════════════
# init-nexus.sh — Inicializa Nexus 3:
#   1. Detecta / cambia la contraseña admin a nexus123
#   2. Crea el repositorio raw hosted "raw-test"
#
# Se llama desde start-test-env.sh después de que Nexus está listo.
# Es IDEMPOTENTE: puede ejecutarse varias veces sin efectos adversos.
# ═══════════════════════════════════════════════════════════════════
set -e

NEXUS_URL="http://localhost:8081"
NEXUS_USER="admin"
NEXUS_TARGET_PASS="nexus123"
NEXUS_CONTAINER="nexus-test"

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

log()  { echo -e "${GREEN}[✓]${NC} $1"; }
warn() { echo -e "${YELLOW}[!]${NC} $1"; }
info() { echo -e "${BLUE}[→]${NC} $1"; }
err()  { echo -e "${RED}[✗]${NC} $1"; }

# ── 1. Detectar contraseña activa ────────────────────────────────
info "Detectando estado de autenticación de Nexus..."

NEXUS_PASS=""

# ¿Ya está configurado con nexus123?
HTTP=$(curl -s -o /dev/null -w "%{http_code}" \
    -u "${NEXUS_USER}:${NEXUS_TARGET_PASS}" \
    "${NEXUS_URL}/service/rest/v1/status/writable" 2>/dev/null || echo "000")

if [ "${HTTP}" = "200" ]; then
    log "Nexus ya está configurado con contraseña estándar (nexus123)"
    NEXUS_PASS="${NEXUS_TARGET_PASS}"
else
    # Intentar con la contraseña inicial del archivo generado por Nexus
    info "Leyendo contraseña inicial desde el contenedor..."
    INITIAL_PASS=$(docker exec "${NEXUS_CONTAINER}" \
        cat /nexus-data/admin.password 2>/dev/null | tr -d '[:space:]' || echo "")

    if [ -z "${INITIAL_PASS}" ]; then
        warn "No se encontró /nexus-data/admin.password — Nexus puede ya estar configurado."
        warn "Intentando con contraseña por defecto 'admin123'..."
        INITIAL_PASS="admin123"
    fi

    # Verificar que la contraseña inicial funciona
    HTTP=$(curl -s -o /dev/null -w "%{http_code}" \
        -u "${NEXUS_USER}:${INITIAL_PASS}" \
        "${NEXUS_URL}/service/rest/v1/status/writable" 2>/dev/null || echo "000")

    if [ "${HTTP}" != "200" ]; then
        err "No se pudo autenticar en Nexus (HTTP ${HTTP}). Revisa el contenedor: docker logs ${NEXUS_CONTAINER}"
        exit 1
    fi

    # ── 2. Cambiar contraseña a nexus123 ─────────────────────────
    info "Cambiando contraseña admin a nexus123..."
    HTTP=$(curl -s -o /dev/null -w "%{http_code}" \
        -X PUT \
        -u "${NEXUS_USER}:${INITIAL_PASS}" \
        -H "Content-Type: text/plain" \
        -d "${NEXUS_TARGET_PASS}" \
        "${NEXUS_URL}/service/rest/v1/security/users/admin/change-password" 2>/dev/null || echo "000")

    if [ "${HTTP}" = "204" ]; then
        log "Contraseña cambiada a nexus123"
        NEXUS_PASS="${NEXUS_TARGET_PASS}"
    else
        warn "No se pudo cambiar contraseña (HTTP ${HTTP}). Continuando con contraseña inicial."
        NEXUS_PASS="${INITIAL_PASS}"
    fi
fi

# ── 3. Aceptar EULA / completar onboarding ───────────────────────
# Nexus ≥ 3.68 bloquea uploads hasta que se acepta el EULA.
# Si el endpoint existe lo aceptamos; si no (versiones anteriores), ignoramos.
info "Verificando/aceptando EULA de Nexus (Nexus ≥ 3.68)..."
EULA_HTTP=$(curl -s -o /dev/null -w "%{http_code}" \
    -X POST \
    -u "${NEXUS_USER}:${NEXUS_PASS}" \
    -H "Content-Type: application/json" \
    "${NEXUS_URL}/service/rest/v1/eula/accept" 2>/dev/null || echo "000")

case "${EULA_HTTP}" in
    200|204) log "EULA aceptado via REST API" ;;
    404)     warn "Endpoint EULA no disponible en esta versión — omitiendo (OK para Nexus < 3.68)" ;;
    *)       warn "Respuesta inesperada al aceptar EULA: HTTP ${EULA_HTTP}" ;;
esac

# Completar wizard de onboarding si está activo
# (deshabilitar anonymousAccessEnabled prompt)
curl -s -o /dev/null -w "" \
    -X PUT \
    -u "${NEXUS_USER}:${NEXUS_PASS}" \
    -H "Content-Type: application/json" \
    -d '{"completed": true}' \
    "${NEXUS_URL}/service/rest/v1/onboarding" 2>/dev/null || true

# ── 5. Habilitar acceso anónimo (lectura sin login) ──────────────
info "Habilitando acceso anónimo en Nexus..."
curl -s -o /dev/null \
    -X PUT \
    -u "${NEXUS_USER}:${NEXUS_PASS}" \
    -H "Content-Type: application/json" \
    -d '{"enabled": true, "userId": "anonymous", "realmName": "NexusAuthorizingRealm"}' \
    "${NEXUS_URL}/service/rest/v1/security/anonymous" 2>/dev/null || true

# ── 6. Crear repositorio raw-test ───────────────────────────────
info "Verificando/creando repositorio 'raw-test' en Nexus..."

# Verificar si ya existe
EXISTING=$(curl -s \
    -u "${NEXUS_USER}:${NEXUS_PASS}" \
    "${NEXUS_URL}/service/rest/v1/repositories" 2>/dev/null \
    | python3 -c "
import sys, json
repos = json.load(sys.stdin)
names = [r['name'] for r in repos]
print('exists' if 'raw-test' in names else 'missing')
" 2>/dev/null || echo "unknown")

if [ "${EXISTING}" = "exists" ]; then
    log "Repositorio 'raw-test' ya existe"
else
    HTTP=$(curl -s -o /tmp/nexus-create.log -w "%{http_code}" \
        -X POST \
        -u "${NEXUS_USER}:${NEXUS_PASS}" \
        -H "Content-Type: application/json" \
        -d '{
            "name": "raw-test",
            "online": true,
            "storage": {
                "blobStoreName": "default",
                "strictContentTypeValidation": false,
                "writePolicy": "allow"
            },
            "raw": {
                "contentDisposition": "ATTACHMENT"
            }
        }' \
        "${NEXUS_URL}/service/rest/v1/repositories/raw/hosted" 2>/dev/null || echo "000")

    case "${HTTP}" in
        201) log "Repositorio 'raw-test' creado exitosamente" ;;
        400|422) warn "Error de validación al crear repo (HTTP ${HTTP}): $(cat /tmp/nexus-create.log)" ;;
        *) warn "Creación de repositorio devolvió HTTP ${HTTP}" ;;
    esac
fi

# ── 7. Resumen ───────────────────────────────────────────────────
echo ""
log "Nexus configurado:"
echo "   URL      → ${NEXUS_URL}"
echo "   Usuario  → ${NEXUS_USER} / ${NEXUS_TARGET_PASS}"
echo "   Repo raw → ${NEXUS_URL}/repository/raw-test/"
echo ""
