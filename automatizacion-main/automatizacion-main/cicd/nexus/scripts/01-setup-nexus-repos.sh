#!/usr/bin/env bash
# =============================================================================
# 01-setup-nexus-repos.sh
# Crea/verifica los repositorios estándar en Nexus 3 vía REST API
# Uso: ./01-setup-nexus-repos.sh [NEXUS_URL] [ADMIN_USER] [ADMIN_PASS]
# Ejemplo: ./01-setup-nexus-repos.sh http://nexus:8081 admin admin123
# =============================================================================
set -euo pipefail

NEXUS_URL="${1:-http://localhost:8081}"
NEXUS_USER="${2:-admin}"
NEXUS_PASS="${3:-admin123}"
API="${NEXUS_URL}/service/rest/v1"

RED='\033[0;31m'; GREEN='\033[0;32m'; YELLOW='\033[1;33m'; NC='\033[0m'

info()  { echo -e "${GREEN}[INFO]${NC}  $*"; }
warn()  { echo -e "${YELLOW}[WARN]${NC}  $*"; }
error() { echo -e "${RED}[ERROR]${NC} $*" >&2; }

# ---------------------------------------------------------------------------
check_nexus() {
  info "Verificando conexión con Nexus en ${NEXUS_URL}..."
  local status
  status=$(curl -s -o /dev/null -w "%{http_code}" -u "${NEXUS_USER}:${NEXUS_PASS}" "${API}/status")
  if [[ "$status" != "200" ]]; then
    error "No se pudo conectar a Nexus (HTTP $status). Verifique URL y credenciales."
    exit 1
  fi
  info "Conexión exitosa."
}

# ---------------------------------------------------------------------------
repo_exists() {
  local name="$1"
  local status
  status=$(curl -s -o /dev/null -w "%{http_code}" -u "${NEXUS_USER}:${NEXUS_PASS}" "${API}/repositories/${name}")
  [[ "$status" == "200" ]]
}

# ---------------------------------------------------------------------------
create_hosted_maven() {
  local name="$1" policy="$2" description="$3"
  if repo_exists "$name"; then
    warn "Repositorio '${name}' ya existe. Omitiendo."
    return
  fi
  info "Creando repositorio hosted Maven: ${name} (policy: ${policy})"
  curl -s -f -u "${NEXUS_USER}:${NEXUS_PASS}" \
    -H "Content-Type: application/json" \
    -X POST "${API}/repositories/maven/hosted" \
    -d "{
      \"name\": \"${name}\",
      \"online\": true,
      \"storage\": {
        \"blobStoreName\": \"default\",
        \"strictContentTypeValidation\": true,
        \"writePolicy\": \"ALLOW\"
      },
      \"maven\": {
        \"versionPolicy\": \"${policy}\",
        \"layoutPolicy\": \"STRICT\",
        \"contentDisposition\": \"ATTACHMENT\"
      }
    }" >/dev/null
  info "  -> Creado: ${name}"
}

# ---------------------------------------------------------------------------
create_proxy_maven() {
  local name="$1" remote_url="$2" description="$3"
  if repo_exists "$name"; then
    warn "Repositorio '${name}' ya existe. Omitiendo."
    return
  fi
  info "Creando repositorio proxy Maven: ${name} -> ${remote_url}"
  curl -s -f -u "${NEXUS_USER}:${NEXUS_PASS}" \
    -H "Content-Type: application/json" \
    -X POST "${API}/repositories/maven/proxy" \
    -d "{
      \"name\": \"${name}\",
      \"online\": true,
      \"storage\": {
        \"blobStoreName\": \"default\",
        \"strictContentTypeValidation\": false
      },
      \"proxy\": {
        \"remoteUrl\": \"${remote_url}\",
        \"contentMaxAge\": 1440,
        \"metadataMaxAge\": 1440
      },
      \"negativeCache\": {
        \"enabled\": true,
        \"timeToLive\": 1440
      },
      \"httpClient\": {
        \"blocked\": false,
        \"autoBlock\": true
      },
      \"maven\": {
        \"versionPolicy\": \"RELEASE\",
        \"layoutPolicy\": \"PERMISSIVE\",
        \"contentDisposition\": \"ATTACHMENT\"
      }
    }" >/dev/null
  info "  -> Creado: ${name}"
}

# ---------------------------------------------------------------------------
create_group_maven() {
  local name="$1"
  shift
  local members=("$@")
  if repo_exists "$name"; then
    warn "Repositorio grupo '${name}' ya existe. Omitiendo."
    return
  fi
  local members_json
  members_json=$(printf '"%s",' "${members[@]}" | sed 's/,$//')
  info "Creando grupo Maven: ${name}"
  curl -s -f -u "${NEXUS_USER}:${NEXUS_PASS}" \
    -H "Content-Type: application/json" \
    -X POST "${API}/repositories/maven/group" \
    -d "{
      \"name\": \"${name}\",
      \"online\": true,
      \"storage\": {
        \"blobStoreName\": \"default\",
        \"strictContentTypeValidation\": true
      },
      \"group\": {
        \"memberNames\": [${members_json}]
      },
      \"maven\": {
        \"contentDisposition\": \"ATTACHMENT\"
      }
    }" >/dev/null
  info "  -> Creado: ${name}"
}

# ---------------------------------------------------------------------------
create_hosted_raw() {
  local name="$1"
  if repo_exists "$name"; then
    warn "Repositorio '${name}' ya existe. Omitiendo."
    return
  fi
  info "Creando repositorio raw: ${name}"
  curl -s -f -u "${NEXUS_USER}:${NEXUS_PASS}" \
    -H "Content-Type: application/json" \
    -X POST "${API}/repositories/raw/hosted" \
    -d "{
      \"name\": \"${name}\",
      \"online\": true,
      \"storage\": {
        \"blobStoreName\": \"default\",
        \"strictContentTypeValidation\": false,
        \"writePolicy\": \"ALLOW\"
      }
    }" >/dev/null
  info "  -> Creado: ${name}"
}

# ---------------------------------------------------------------------------
main() {
  echo "============================================================"
  echo "  Setup de repositorios Nexus - Infraestructura CI/CD"
  echo "  Nexus: ${NEXUS_URL}"
  echo "============================================================"

  check_nexus

  echo ""
  info "=== Repositorios Java/Maven ==="
  create_hosted_maven "maven-releases"  "RELEASE"  "Artefactos Java estables"
  create_hosted_maven "maven-snapshots" "SNAPSHOT" "Artefactos Java en desarrollo"
  create_proxy_maven  "maven-central-proxy" "https://repo1.maven.org/maven2/" "Cache Maven Central"

  echo ""
  info "=== Repositorios Mule ESB ==="
  create_hosted_maven "mule-releases"  "RELEASE"  "Apps Mule estables"
  create_hosted_maven "mule-snapshots" "SNAPSHOT" "Apps Mule en desarrollo"
  create_proxy_maven  "mulesoft-proxy" "https://repository.mulesoft.org/nexus/content/repositories/releases/" "Cache MuleSoft"

  echo ""
  info "=== Repositorios Legacy/Migración ==="
  create_hosted_raw "legacy-artifacts"

  echo ""
  info "=== Grupo público (punto único de resolución) ==="
  create_group_maven "maven-public" \
    "maven-releases" "maven-snapshots" "maven-central-proxy" \
    "mule-releases"  "mule-snapshots"  "mulesoft-proxy"

  echo ""
  echo "============================================================"
  info "Setup completado."
  echo ""
  echo "URLs de los repositorios:"
  echo "  Releases :  ${NEXUS_URL}/repository/maven-releases/"
  echo "  Snapshots:  ${NEXUS_URL}/repository/maven-snapshots/"
  echo "  Mule     :  ${NEXUS_URL}/repository/mule-releases/"
  echo "  Legacy   :  ${NEXUS_URL}/repository/legacy-artifacts/"
  echo "  Grupo    :  ${NEXUS_URL}/repository/maven-public/  ← usar en settings.xml"
  echo "============================================================"
}

main "$@"
