#!/bin/bash
# ═══════════════════════════════════════════════════════════════════
# start-test-env.sh — Script maestro para levantar el ambiente
# de prueba: Jenkins + Nexus + 3 Servers Destino
#
# Uso:  bash setup/start-test-env.sh
#       bash setup/start-test-env.sh --reset    ← borra volúmenes
# ═══════════════════════════════════════════════════════════════════
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ENV_DIR="${SCRIPT_DIR}/.."
COMPOSE_FILE="${ENV_DIR}/docker-compose.test.yml"
ENV_FILE="${ENV_DIR}/.env.test"
KEYS_DIR="${SCRIPT_DIR}/ssh-keys"
RESET_VOLUMES=false

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

log()  { echo -e "${GREEN}[✓]${NC} $1"; }
warn() { echo -e "${YELLOW}[!]${NC} $1"; }
info() { echo -e "${BLUE}[→]${NC} $1"; }
err()  { echo -e "${RED}[✗]${NC} $1"; exit 1; }

# Procesar argumentos
for arg in "$@"; do
    case $arg in
        --reset) RESET_VOLUMES=true ;;
    esac
done

echo ""
echo "═══════════════════════════════════════════════════════════"
echo "   Ambiente de Prueba: Jenkins + Nexus + Servers Destino   "
echo "═══════════════════════════════════════════════════════════"
echo ""

# ── 1. Prerequisitos ────────────────────────────────────────────
info "Verificando prerequisitos..."
command -v docker >/dev/null 2>&1 || err "Docker no está instalado"
docker compose version >/dev/null 2>&1 || err "Docker Compose no está instalado"
command -v python3 >/dev/null 2>&1 || err "python3 no está instalado (requerido para parsing JSON)"
log "Docker, Docker Compose y python3 disponibles"

# Advertir si GITHUB_TOKEN no está seteado (requerido para repos privados)
if [ -z "${GITHUB_TOKEN}" ]; then
    warn "GITHUB_TOKEN no está definido."
    warn "  → Si el repo 'infra-neo/automatizacion' es PÚBLICO, puedes ignorar esto."
    warn "  → Si es PRIVADO, el job 'maven' fallará al clonar el repositorio."
    warn "  → Para configurarlo: export GITHUB_TOKEN=ghp_xxxxxxxxxxxxxxxxxxxx"
else
    log "GITHUB_TOKEN detectado — credencial GitHub será configurada en Jenkins"
fi

# ── 2. Reset opcional ────────────────────────────────────────────
if [ "${RESET_VOLUMES}" = "true" ]; then
    warn "Modo RESET: eliminando contenedores y volúmenes existentes..."
    docker compose -f "${COMPOSE_FILE}" down -v --remove-orphans 2>/dev/null || true
    log "Ambiente anterior eliminado"
fi

# ── 3. Generar claves SSH ────────────────────────────────────────
info "Generando claves SSH..."
bash "${SCRIPT_DIR}/generate-ssh-keys.sh"
log "Claves SSH listas en ${KEYS_DIR}/"

# ── 4. Exportar clave privada para JCasC (via env var) ──────────
info "Preparando credencial SSH para Jenkins (JCasC)..."
export SSH_PRIVATE_KEY
SSH_PRIVATE_KEY="$(cat "${KEYS_DIR}/jenkins_rsa")"

# ── 5. Build de imágenes ─────────────────────────────────────────
info "Construyendo imágenes Docker (esto puede tardar varios minutos)..."
info "  └─ Jenkins: descargando plugins desde Jenkins Update Center..."
SSH_PRIVATE_KEY="${SSH_PRIVATE_KEY}" \
docker compose -f "${COMPOSE_FILE}" --env-file "${ENV_FILE}" build --no-cache
log "Imágenes construidas"

# ── 6. Levantar servicios ────────────────────────────────────────
info "Levantando contenedores..."
SSH_PRIVATE_KEY="${SSH_PRIVATE_KEY}" \
docker compose -f "${COMPOSE_FILE}" --env-file "${ENV_FILE}" up -d
log "Contenedores iniciados"

# ── 7. Esperar a Jenkins ─────────────────────────────────────────
info "Esperando que Jenkins esté disponible (2-3 minutos)..."
MAX_WAIT=180
ELAPSED=0
until curl -s -o /dev/null -w "%{http_code}" \
        http://localhost:8080/login 2>/dev/null | grep -q "200\|403"; do
    if [ $ELAPSED -ge $MAX_WAIT ]; then
        err "Timeout esperando Jenkins. Revisa: docker logs jenkins-test"
    fi
    sleep 5
    ELAPSED=$((ELAPSED + 5))
    echo -n "."
done
echo ""
log "Jenkins disponible en http://localhost:8080"

# ── 8. Esperar a Nexus ───────────────────────────────────────────
info "Esperando que Nexus esté disponible (2-4 minutos)..."
ELAPSED=0
until curl -s -o /dev/null -w "%{http_code}" \
        http://localhost:8081/ 2>/dev/null | grep -q "200\|302"; do
    if [ $ELAPSED -ge 240 ]; then
        warn "Nexus tardando más de lo esperado. Intentando continuar..."
        break
    fi
    sleep 5
    ELAPSED=$((ELAPSED + 5))
    echo -n "."
done
echo ""
log "Nexus disponible en http://localhost:8081"

# Nexus necesita unos segundos extra para terminar de inicializar su API REST
info "Esperando que la API REST de Nexus termine de inicializar..."
sleep 20

# ── 9. Inicializar Nexus ─────────────────────────────────────────
info "Configurando Nexus (contraseña + repositorio raw-test)..."
bash "${SCRIPT_DIR}/init-nexus.sh" || warn "init-nexus.sh tuvo problemas. Ver log arriba."

# ── 10. Verificar que JCasC cargó la credencial SSH ─────────────
info "Verificando credencial SSH en Jenkins (JCasC)..."
sleep 15

JENKINS_URL="http://localhost:8080"
JENKINS_USER="admin"
JENKINS_PASS="admin123"

# Obtener crumb CSRF
CRUMB=$(curl -s -u "${JENKINS_USER}:${JENKINS_PASS}" \
    "${JENKINS_URL}/crumbIssuer/api/json" \
    | python3 -c "
import sys, json
try:
    d = json.load(sys.stdin)
    print(d['crumbRequestField'] + ':' + d['crumb'])
except:
    print('')
" 2>/dev/null || echo "")

if [ -z "${CRUMB}" ]; then
    warn "No se pudo obtener crumb CSRF de Jenkins. Jenkins puede seguir cargando."
    warn "Reintentando en 20s..."
    sleep 20
    CRUMB=$(curl -s -u "${JENKINS_USER}:${JENKINS_PASS}" \
        "${JENKINS_URL}/crumbIssuer/api/json" \
        | python3 -c "
import sys, json
try:
    d = json.load(sys.stdin)
    print(d['crumbRequestField'] + ':' + d['crumb'])
except:
    print('')
" 2>/dev/null || echo "")
fi

# Verificar si la credencial ya existe (creada por JCasC)
CRED_EXISTS=$(curl -s -o /dev/null -w "%{http_code}" \
    -u "${JENKINS_USER}:${JENKINS_PASS}" \
    "${JENKINS_URL}/credentials/store/system/domain/_/credential/ssh-deploy-key/api/json" \
    2>/dev/null || echo "000")

PRIVATE_KEY_CONTENT="$(cat "${KEYS_DIR}/jenkins_rsa")"

if [ "${CRED_EXISTS}" = "200" ]; then
    log "Credencial 'ssh-deploy-key' detectada (creada por JCasC)"
    # Actualizar para asegurar que tiene la clave privada correcta
    if [ -n "${CRUMB}" ]; then
        curl -s -o /dev/null \
            -X POST \
            -u "${JENKINS_USER}:${JENKINS_PASS}" \
            -H "${CRUMB}" \
            "${JENKINS_URL}/credentials/store/system/domain/_/credential/ssh-deploy-key/config.xml" \
            -H "Content-Type: application/xml" \
            -d "<?xml version='1.1' encoding='UTF-8'?>
<com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey plugin='ssh-credentials'>
  <scope>GLOBAL</scope>
  <id>ssh-deploy-key</id>
  <description>Clave SSH para acceso a servers destino</description>
  <username>deploy</username>
  <usernameSecret>false</usernameSecret>
  <privateKeySource class='com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey\$DirectEntryPrivateKeySource'>
    <privateKey>${PRIVATE_KEY_CONTENT}</privateKey>
  </privateKeySource>
</com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey>" \
            2>/dev/null || true
        log "Credencial SSH actualizada con clave privada correcta"
    fi
else
    # Crear credencial si JCasC no la creó (plugin no cargado aún)
    warn "Credencial no encontrada via JCasC. Creando via REST API..."
    if [ -n "${CRUMB}" ]; then
        HTTP=$(curl -s -o /dev/null -w "%{http_code}" \
            -X POST \
            -u "${JENKINS_USER}:${JENKINS_PASS}" \
            -H "${CRUMB}" \
            "${JENKINS_URL}/credentials/store/system/domain/_/createCredentials" \
            --data-urlencode "json={
                \"\": \"0\",
                \"credentials\": {
                    \"scope\": \"GLOBAL\",
                    \"id\": \"ssh-deploy-key\",
                    \"description\": \"Clave SSH para acceso a servers destino\",
                    \"username\": \"deploy\",
                    \"privateKeySource\": {
                        \"stapler-class\": \"com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey\$DirectEntryPrivateKeySource\",
                        \"privateKey\": \"${PRIVATE_KEY_CONTENT}\"
                    },
                    \"stapler-class\": \"com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey\"
                }
            }" 2>/dev/null || echo "000")
        if [ "${HTTP}" = "200" ] || [ "${HTTP}" = "302" ]; then
            log "Credencial SSH creada via REST API"
        else
            warn "REST API respondió HTTP ${HTTP}. Verifica la credencial manualmente en Jenkins UI."
        fi
    else
        warn "Sin crumb CSRF — no se pudo crear credencial. Hazlo manualmente:"
        warn "  Jenkins → Manage → Credentials → Global → Add Credential"
        warn "  Kind: SSH Username with private key | ID: ssh-deploy-key"
        warn "  Username: deploy | Private key: $(cat "${KEYS_DIR}/jenkins_rsa")"
    fi
fi

# ── 11. Verificar conectividad SSH a los servers destino ─────────
info "Verificando conectividad SSH a los servers destino..."
ALL_SSH_OK=true
for port in 2221 2222 2223; do
    server_num=$((port - 2220))
    if ssh -i "${KEYS_DIR}/jenkins_rsa" \
        -o StrictHostKeyChecking=no \
        -o ConnectTimeout=5 \
        -p "${port}" deploy@localhost \
        "echo OK" >/dev/null 2>&1; then
        log "target-server-${server_num} ✓ SSH OK (puerto ${port})"
    else
        warn "target-server-${server_num} no responde SSH aún (puerto ${port})"
        ALL_SSH_OK=false
    fi
done

if [ "${ALL_SSH_OK}" = "false" ]; then
    warn "Algunos servers no responden SSH. Los contenedores pueden seguir iniciando."
    warn "Reintenta en 30s: ssh -i ${KEYS_DIR}/jenkins_rsa -p 2221 deploy@localhost"
fi

# ── 12. Resumen ──────────────────────────────────────────────────
echo ""
echo "═══════════════════════════════════════════════════════════"
echo "   AMBIENTE LISTO                                          "
echo "═══════════════════════════════════════════════════════════"
echo ""
echo "  ORQUESTADORES:"
echo "  ┌─────────────────────────────────────────────────────┐"
echo "  │ Jenkins  →  http://localhost:8080  (admin/admin123) │"
echo "  │ Nexus    →  http://localhost:8081  (admin/nexus123) │"
echo "  └─────────────────────────────────────────────────────┘"
echo ""
echo "  SERVERS DESTINO (SSH):"
echo "  ┌─────────────────────────────────────────────────────┐"
echo "  │ target-server-1  →  ssh -p 2221 deploy@localhost   │"
echo "  │ target-server-2  →  ssh -p 2222 deploy@localhost   │"
echo "  │ target-server-3  →  ssh -p 2223 deploy@localhost   │"
echo "  └─────────────────────────────────────────────────────┘"
echo ""
echo "  CREDENCIAL JENKINS (ID: ssh-deploy-key):"
echo "  └─ Clave privada: ${KEYS_DIR}/jenkins_rsa"
echo ""
echo "  NEXUS REPOSITORIO:"
echo "  └─ http://localhost:8081/repository/raw-test/"
echo ""
echo "  PRÓXIMOS PASOS (job maven - auto-creado via JCasC):"
echo "  1. Abre Jenkins → http://localhost:8080  (admin / admin123)"
echo "  2. El job 'maven' aparece automáticamente (Pipeline script from SCM)"
echo "     Fuente: jenkins/Jenkinsfile-java-maven-test @ github.com/infra-neo/automatizacion"
echo "  3. Click en 'maven' → Build with Parameters → selecciona entorno"
echo ""
echo "  PIPELINE FILE-TRANSFER (creación manual):"
echo "  1. New Item → Pipeline → pegar jenkins/Jenkinsfile-file-transfer"
echo ""
echo "  ÚTIL:"
echo "  ├─ Logs:   docker compose -f docker-compose.test.yml logs -f"
echo "  ├─ Reset:  bash setup/start-test-env.sh --reset"
echo "  └─ Apagar: docker compose -f docker-compose.test.yml down"
echo ""
