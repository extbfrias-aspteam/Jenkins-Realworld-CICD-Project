#!/usr/bin/env bash
# =============================================================================
# 02-upload-existing-artifacts.sh
# Sube un artefacto individual a Nexus con coordenadas Maven completas.
# Script de apoyo para el proceso de migración manual.
#
# Uso:
#   ./02-upload-existing-artifacts.sh \
#       --file     /ruta/artefacto.jar \
#       --group    com.empresa.app \
#       --artifact mi-app \
#       --version  1.0.0 \
#       --nexus    http://nexus:8081 \
#       --user     admin \
#       --pass     admin123
#
# También acepta WAR y EAR.
# =============================================================================
set -euo pipefail

GREEN='\033[0;32m'; RED='\033[0;31m'; YELLOW='\033[1;33m'; NC='\033[0m'
info()  { echo -e "${GREEN}[INFO]${NC}  $*"; }
error() { echo -e "${RED}[ERROR]${NC} $*" >&2; }
warn()  { echo -e "${YELLOW}[WARN]${NC}  $*"; }

FILE="" GROUP_ID="" ARTIFACT_ID="" VERSION=""
NEXUS_URL="http://localhost:8081"
NEXUS_USER="" NEXUS_PASS=""
EXTRA_CLASSIFIER=""

# ---------------------------------------------------------------------------
usage() {
  echo "Uso: $0 --file <jar> --group <groupId> --artifact <artifactId> --version <ver> --nexus <url> --user <u> --pass <p>"
  exit 1
}

while [[ $# -gt 0 ]]; do
  case "$1" in
    --file)       FILE="$2"; shift 2 ;;
    --group)      GROUP_ID="$2"; shift 2 ;;
    --artifact)   ARTIFACT_ID="$2"; shift 2 ;;
    --version)    VERSION="$2"; shift 2 ;;
    --nexus)      NEXUS_URL="$2"; shift 2 ;;
    --user)       NEXUS_USER="$2"; shift 2 ;;
    --pass)       NEXUS_PASS="$2"; shift 2 ;;
    --classifier) EXTRA_CLASSIFIER="$2"; shift 2 ;;
    *) error "Argumento desconocido: $1"; usage ;;
  esac
done

[[ -z "$FILE" || -z "$GROUP_ID" || -z "$ARTIFACT_ID" || -z "$VERSION" ]] && usage
[[ -z "$NEXUS_USER" || -z "$NEXUS_PASS" ]] && {
  warn "Usuario/password no provistos. Usando variables de entorno NEXUS_USER/NEXUS_PASS."
  NEXUS_USER="${NEXUS_USER:-$NEXUS_USER}"
  NEXUS_PASS="${NEXUS_PASS:-$NEXUS_PASS}"
}
[[ ! -f "$FILE" ]] && { error "Archivo no encontrado: ${FILE}"; exit 1; }

EXT="${FILE##*.}"
NEXUS_REPO=$(echo "$VERSION" | grep -q "SNAPSHOT" && echo "maven-snapshots" || echo "maven-releases")
# Detectar si es Mule buscando mule-artifact.json dentro del JAR
if command -v unzip &>/dev/null; then
  if unzip -l "$FILE" 2>/dev/null | grep -q "mule-artifact.json\|mule-app.properties"; then
    NEXUS_REPO=$(echo "$VERSION" | grep -q "SNAPSHOT" && echo "mule-snapshots" || echo "mule-releases")
  fi
fi

echo "==========================================================="
info "Publicando artefacto en Nexus"
echo "  Archivo   : ${FILE}"
echo "  GAV       : ${GROUP_ID}:${ARTIFACT_ID}:${VERSION}"
echo "  Tipo      : ${EXT}"
echo "  Repositorio: ${NEXUS_REPO}"
echo "  Nexus URL : ${NEXUS_URL}"
echo "==========================================================="

CLASSIFIER_OPT=""
[[ -n "$EXTRA_CLASSIFIER" ]] && CLASSIFIER_OPT="-Dclassifier=${EXTRA_CLASSIFIER}"

mvn deploy:deploy-file \
    -DgroupId="${GROUP_ID}" \
    -DartifactId="${ARTIFACT_ID}" \
    -Dversion="${VERSION}" \
    -Dpackaging="${EXT}" \
    -Dfile="${FILE}" \
    ${CLASSIFIER_OPT} \
    -DrepositoryId="nexus-upload" \
    -Durl="${NEXUS_URL}/repository/${NEXUS_REPO}/" \
    -s <(cat <<EOF
<settings>
  <servers>
    <server>
      <id>nexus-upload</id>
      <username>${NEXUS_USER}</username>
      <password>${NEXUS_PASS}</password>
    </server>
  </servers>
</settings>
EOF
    ) \
    -q

echo ""
info "Artefacto publicado exitosamente."
echo "  URL: ${NEXUS_URL}/repository/${NEXUS_REPO}/${GROUP_ID//.//}/${ARTIFACT_ID}/${VERSION}/"
