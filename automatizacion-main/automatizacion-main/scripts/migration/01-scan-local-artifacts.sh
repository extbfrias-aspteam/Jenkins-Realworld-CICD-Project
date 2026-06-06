#!/usr/bin/env bash
# =============================================================================
# 01-scan-local-artifacts.sh
# TAREA PRIORITARIA: Escanear la máquina local en busca de artefactos
# Java compilados (JAR, WAR, EAR) fuera del repositorio de código fuente.
#
# Uso: ./01-scan-local-artifacts.sh [directorio_base] [archivo_salida.csv]
# Ejemplo: ./01-scan-local-artifacts.sh /home /tmp/artefactos-encontrados.csv
#          ./01-scan-local-artifacts.sh C:/Users  artefactos.csv   (Git Bash)
#
# El CSV resultante se usa como entrada para 02-bulk-upload.sh
# =============================================================================
set -euo pipefail

SCAN_DIR="${1:-$HOME}"
OUTPUT_CSV="${2:-artefactos-encontrados-$(date +%Y%m%d-%H%M%S).csv}"
SKIP_DIRS=".m2 .gradle node_modules __pycache__ .git target/dependency"

GREEN='\033[0;32m'; YELLOW='\033[1;33m'; CYAN='\033[0;36m'; NC='\033[0m'
info()  { echo -e "${GREEN}[INFO]${NC}  $*"; }
warn()  { echo -e "${YELLOW}[WARN]${NC}  $*"; }
found() { echo -e "${CYAN}[FOUND]${NC} $*"; }

# ---------------------------------------------------------------------------
# Extrae metadatos básicos del artefacto
analyze_artifact() {
  local filepath="$1"
  local filename extension size group_id artifact_id version repo_target

  filename=$(basename "$filepath")
  extension="${filename##*.}"
  size=$(du -sh "$filepath" 2>/dev/null | cut -f1 || echo "?")

  # Intentar leer pom.properties del JAR/WAR para extraer groupId/artifactId/version
  group_id=""
  artifact_id=""
  version=""

  if command -v unzip &>/dev/null; then
    local pom_props
    pom_props=$(unzip -p "$filepath" "*/pom.properties" 2>/dev/null | head -20 || true)
    if [[ -n "$pom_props" ]]; then
      group_id=$(echo "$pom_props"    | grep "^groupId="    | cut -d= -f2 | tr -d '\r')
      artifact_id=$(echo "$pom_props" | grep "^artifactId=" | cut -d= -f2 | tr -d '\r')
      version=$(echo "$pom_props"     | grep "^version="    | cut -d= -f2 | tr -d '\r')
    fi
  fi

  # Si no se pudo leer, inferir desde el nombre del archivo
  if [[ -z "$artifact_id" ]]; then
    # Intentar patrón: nombre-version.jar → nombre=artifactId, version
    if [[ "$filename" =~ ^(.+)-([0-9]+\.[0-9]+.*)\.(jar|war|ear)$ ]]; then
      artifact_id="${BASH_REMATCH[1]}"
      version="${BASH_REMATCH[2]}"
    else
      artifact_id="${filename%.*}"
      version="UNKNOWN"
    fi
    group_id="com.empresa.legacy"
  fi

  # Determinar repositorio destino en Nexus
  if [[ "$version" == *"SNAPSHOT"* ]]; then
    repo_target="maven-snapshots"
  elif [[ "$version" == "UNKNOWN" ]]; then
    repo_target="legacy-artifacts"
  else
    repo_target="maven-releases"
  fi

  # Detectar si parece app Mule
  if command -v unzip &>/dev/null; then
    if unzip -l "$filepath" 2>/dev/null | grep -q "mule-app.properties\|mule-artifact.json"; then
      if [[ "$version" == *"SNAPSHOT"* ]]; then
        repo_target="mule-snapshots"
      else
        repo_target="mule-releases"
      fi
    fi
  fi

  echo "\"${filepath}\",\"${filename}\",\"${extension}\",\"${size}\",\"${group_id}\",\"${artifact_id}\",\"${version}\",\"${repo_target}\""
}

# ---------------------------------------------------------------------------
main() {
  echo "==========================================================="
  echo "  Escaneo de artefactos Java compilados"
  echo "  Directorio base : ${SCAN_DIR}"
  echo "  Archivo salida  : ${OUTPUT_CSV}"
  echo "==========================================================="
  echo ""

  # Build skip-dir expression for find
  local prune_expr=""
  for d in $SKIP_DIRS; do
    prune_expr="${prune_expr} -o -path '*/${d}/*' -prune"
  done

  # Header del CSV
  echo '"ruta_completa","nombre_archivo","extension","tamanio","group_id","artifact_id","version","repo_nexus_destino"' > "$OUTPUT_CSV"

  local total=0

  info "Escaneando (esto puede tardar unos minutos)..."

  # Buscar JARs, WARs y EARs excluyendo directorios de dependencias
  while IFS= read -r filepath; do
    # Omitir artefactos dentro de repositorios .m2 (ya están en Nexus o Maven Central)
    if echo "$filepath" | grep -q "/.m2/\|/.gradle/"; then
      continue
    fi
    # Omitir artefactos test (nombre contiene -test, -tests)
    local bn
    bn=$(basename "$filepath")
    if [[ "$bn" == *"-test."* ]] || [[ "$bn" == *"-tests."* ]]; then
      continue
    fi

    found "$filepath"
    analyze_artifact "$filepath" >> "$OUTPUT_CSV"
    total=$((total + 1))
  done < <(eval "find \"${SCAN_DIR}\" \( -false ${prune_expr} \) -prune -o \( -name '*.jar' -o -name '*.war' -o -name '*.ear' \) -print 2>/dev/null")

  echo ""
  echo "==========================================================="
  info "Escaneo completado."
  echo "  Total artefactos encontrados : ${total}"
  echo "  Archivo CSV generado         : ${OUTPUT_CSV}"
  echo ""
  echo "PRÓXIMO PASO:"
  echo "  Revisar el CSV y luego ejecutar:"
  echo "  ./02-bulk-upload.sh ${OUTPUT_CSV} http://NEXUS_HOST:8081 usuario password"
  echo "==========================================================="
}

main "$@"
