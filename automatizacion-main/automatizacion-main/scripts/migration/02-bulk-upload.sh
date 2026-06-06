#!/usr/bin/env bash
# =============================================================================
# 02-bulk-upload.sh
# TAREA PRIORITARIA: Sube en lote los artefactos del CSV generado por
# 01-scan-local-artifacts.sh a Nexus Repository Manager.
#
# Uso:
#   ./02-bulk-upload.sh <artefactos.csv> <NEXUS_URL> <USUARIO> <PASSWORD>
#
# Ejemplo:
#   ./02-bulk-upload.sh artefactos-20240324.csv http://192.168.1.100:8081 admin admin123
#
# También se puede usar para subir un solo artefacto de forma interactiva
# llamando sin argumentos.
# =============================================================================
set -euo pipefail

INPUT_CSV="${1:-}"
NEXUS_URL="${2:-}"
NEXUS_USER="${3:-}"
NEXUS_PASS="${4:-}"

LOG_FILE="upload-log-$(date +%Y%m%d-%H%M%S).log"

RED='\033[0;31m'; GREEN='\033[0;32m'; YELLOW='\033[1;33m'; CYAN='\033[0;36m'; NC='\033[0m'
info()    { echo -e "${GREEN}[INFO]${NC}   $*" | tee -a "$LOG_FILE"; }
warn()    { echo -e "${YELLOW}[WARN]${NC}   $*" | tee -a "$LOG_FILE"; }
error()   { echo -e "${RED}[ERROR]${NC}  $*" | tee -a "$LOG_FILE" >&2; }
success() { echo -e "${GREEN}[OK]${NC}     $*" | tee -a "$LOG_FILE"; }

# ---------------------------------------------------------------------------
usage() {
  echo ""
  echo "Uso: $0 <artefactos.csv> <NEXUS_URL> <USUARIO> <PASSWORD>"
  echo ""
  echo "El CSV debe tener el formato generado por 01-scan-local-artifacts.sh"
  echo "Columnas: ruta_completa, nombre_archivo, extension, tamanio,"
  echo "          group_id, artifact_id, version, repo_nexus_destino"
  echo ""
  exit 1
}

# ---------------------------------------------------------------------------
check_dependencies() {
  local missing=()
  command -v curl    &>/dev/null || missing+=("curl")
  command -v mvn     &>/dev/null || missing+=("mvn (Maven)")
  if [[ ${#missing[@]} -gt 0 ]]; then
    error "Dependencias faltantes: ${missing[*]}"
    error "Instalar antes de continuar."
    exit 1
  fi
}

# ---------------------------------------------------------------------------
check_nexus_connection() {
  info "Verificando conexión con Nexus en ${NEXUS_URL}..."
  local status
  status=$(curl -s -o /dev/null -w "%{http_code}" \
    -u "${NEXUS_USER}:${NEXUS_PASS}" \
    "${NEXUS_URL}/service/rest/v1/status" || echo "000")
  if [[ "$status" != "200" ]]; then
    error "No se puede conectar a Nexus (HTTP $status). Verifique URL y credenciales."
    exit 1
  fi
  info "Conexión OK."
}

# ---------------------------------------------------------------------------
# Sube un artefacto a Nexus usando el REST API de componentes
upload_to_nexus_raw() {
  local filepath="$1" repo="$2" filename="$3"
  local status
  status=$(curl -s -o /dev/null -w "%{http_code}" \
    -u "${NEXUS_USER}:${NEXUS_PASS}" \
    -X POST "${NEXUS_URL}/service/rest/v1/components?repository=${repo}" \
    -F "raw.directory=/" \
    -F "raw.asset1=@${filepath};type=application/octet-stream" \
    -F "raw.asset1.filename=${filename}" || echo "000")
  echo "$status"
}

# ---------------------------------------------------------------------------
# Sube un artefacto Maven con coordenadas correctas a Nexus
upload_to_nexus_maven() {
  local filepath="$1" repo="$2" group_id="$3" artifact_id="$4" version="$5" ext="$6"

  # Usar maven deploy:deploy-file (más confiable que la API REST para Maven)
  local nexus_repo_url="${NEXUS_URL}/repository/${repo}/"
  local server_id="nexus-upload-temp"

  local mvn_output
  if mvn deploy:deploy-file \
    -DgroupId="${group_id}" \
    -DartifactId="${artifact_id}" \
    -Dversion="${version}" \
    -Dpackaging="${ext}" \
    -Dfile="${filepath}" \
    -DrepositoryId="${server_id}" \
    -Durl="${nexus_repo_url}" \
    -s <(cat <<EOF
<settings>
  <servers>
    <server>
      <id>${server_id}</id>
      <username>${NEXUS_USER}</username>
      <password>${NEXUS_PASS}</password>
    </server>
  </servers>
</settings>
EOF
    ) \
    -q 2>&1; then
    echo "200"
  else
    echo "500"
  fi
}

# ---------------------------------------------------------------------------
# Verifica si el artefacto ya existe en Nexus (evita duplicados)
artifact_exists_in_nexus() {
  local repo="$1" group_id="$2" artifact_id="$3" version="$4"
  local group_path="${group_id//.//}"
  local status
  status=$(curl -s -o /dev/null -w "%{http_code}" \
    -u "${NEXUS_USER}:${NEXUS_PASS}" \
    "${NEXUS_URL}/repository/${repo}/${group_path}/${artifact_id}/${version}/" 2>/dev/null || echo "000")
  [[ "$status" == "200" ]]
}

# ---------------------------------------------------------------------------
process_csv() {
  local csv_file="$1"
  local total=0 uploaded=0 skipped=0 failed=0

  info "Procesando CSV: ${csv_file}"
  echo ""

  # Leer CSV omitiendo header
  while IFS=',' read -r filepath filename extension size group_id artifact_id version repo_target; do
    # Limpiar comillas del CSV
    filepath="${filepath//\"/}"
    filename="${filename//\"/}"
    extension="${extension//\"/}"
    group_id="${group_id//\"/}"
    artifact_id="${artifact_id//\"/}"
    version="${version//\"/}"
    repo_target="${repo_target//\"/}"

    # Omitir línea de header
    [[ "$filepath" == "ruta_completa" ]] && continue
    # Omitir líneas vacías
    [[ -z "$filepath" ]] && continue

    total=$((total + 1))

    echo "-----------------------------------------------------------"
    echo "  Artefacto #${total}: ${filename}"
    echo "  Ruta    : ${filepath}"
    echo "  GAV     : ${group_id}:${artifact_id}:${version}"
    echo "  Repo    : ${repo_target}"

    # Verificar que el archivo existe
    if [[ ! -f "$filepath" ]]; then
      warn "Archivo no encontrado: ${filepath}. Omitiendo."
      skipped=$((skipped + 1))
      continue
    fi

    # Verificar si ya existe en Nexus
    if [[ "$repo_target" != "legacy-artifacts" ]] && \
       artifact_exists_in_nexus "$repo_target" "$group_id" "$artifact_id" "$version"; then
      warn "Ya existe en Nexus ${repo_target}. Omitiendo."
      skipped=$((skipped + 1))
      continue
    fi

    # Subir según tipo de repositorio
    local status
    if [[ "$repo_target" == "legacy-artifacts" ]]; then
      info "Subiendo como raw a legacy-artifacts..."
      status=$(upload_to_nexus_raw "$filepath" "$repo_target" "$filename")
    else
      info "Subiendo como artefacto Maven a ${repo_target}..."
      status=$(upload_to_nexus_maven "$filepath" "$repo_target" "$group_id" "$artifact_id" "$version" "$extension")
    fi

    if [[ "$status" == "200" || "$status" == "201" || "$status" == "204" ]]; then
      success "Subido correctamente: ${group_id}:${artifact_id}:${version} -> ${repo_target}"
      uploaded=$((uploaded + 1))
    else
      error "Fallo al subir ${filename} (status: ${status})"
      failed=$((failed + 1))
    fi

  done < "$csv_file"

  echo ""
  echo "==========================================================="
  info "Migración completada."
  echo "  Total procesados : ${total}"
  echo "  Subidos          : ${uploaded}"
  echo "  Omitidos         : ${skipped}"
  echo "  Fallidos         : ${failed}"
  echo "  Log guardado     : ${LOG_FILE}"
  echo "==========================================================="

  [[ $failed -gt 0 ]] && return 1 || return 0
}

# ---------------------------------------------------------------------------
interactive_mode() {
  echo ""
  echo "Modo interactivo: subir un artefacto individual"
  echo "-----------------------------------------------"
  read -rp "Ruta del archivo (JAR/WAR/EAR): " filepath
  [[ ! -f "$filepath" ]] && { error "Archivo no encontrado."; exit 1; }
  read -rp "Nexus URL (ej: http://192.168.1.100:8081): " NEXUS_URL
  read -rp "Usuario Nexus: " NEXUS_USER
  read -rsp "Password Nexus: " NEXUS_PASS; echo ""
  read -rp "GroupId (ej: com.empresa.app): " group_id
  read -rp "ArtifactId (ej: mi-app): " artifact_id
  read -rp "Versión (ej: 1.0.0 o 1.0.0-SNAPSHOT): " version
  read -rp "Repositorio Nexus destino (maven-releases/maven-snapshots/mule-releases/legacy-artifacts): " repo_target
  local ext="${filepath##*.}"

  check_nexus_connection

  local status
  if [[ "$repo_target" == "legacy-artifacts" ]]; then
    status=$(upload_to_nexus_raw "$filepath" "$repo_target" "$(basename "$filepath")")
  else
    status=$(upload_to_nexus_maven "$filepath" "$repo_target" "$group_id" "$artifact_id" "$version" "$ext")
  fi

  if [[ "$status" == "200" || "$status" == "201" || "$status" == "204" ]]; then
    success "Artefacto subido exitosamente."
    echo "  URL: ${NEXUS_URL}/repository/${repo_target}/${group_id//.//}/${artifact_id}/${version}/"
  else
    error "Fallo al subir artefacto (HTTP ${status})"
    exit 1
  fi
}

# ---------------------------------------------------------------------------
main() {
  echo "==========================================================="
  echo "  Carga masiva de artefactos a Nexus"
  echo "  Log: ${LOG_FILE}"
  echo "==========================================================="

  check_dependencies

  if [[ -z "$INPUT_CSV" ]]; then
    interactive_mode
    exit 0
  fi

  [[ -z "$NEXUS_URL" || -z "$NEXUS_USER" || -z "$NEXUS_PASS" ]] && usage
  [[ ! -f "$INPUT_CSV" ]] && { error "CSV no encontrado: ${INPUT_CSV}"; usage; }

  check_nexus_connection
  process_csv "$INPUT_CSV"
}

main "$@"
