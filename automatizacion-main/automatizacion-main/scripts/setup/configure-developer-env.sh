#!/usr/bin/env bash
# =============================================================================
# configure-developer-env.sh
# Configura el entorno de desarrollo de un técnico/developer para usar
# Nexus como repositorio central de dependencias y destino de artefactos.
#
# Uso: ./configure-developer-env.sh [NEXUS_HOST] [NEXUS_PORT] [tipo: java|mule|ambos]
# Ejemplo: ./configure-developer-env.sh 192.168.1.100 8081 java
# =============================================================================
set -euo pipefail

NEXUS_HOST="${1:-}"
NEXUS_PORT="${2:-8081}"
PROJECT_TYPE="${3:-java}"  # java | mule | ambos
TEMPLATES_DIR="$(cd "$(dirname "$0")/../../templates" && pwd)"

GREEN='\033[0;32m'; YELLOW='\033[1;33m'; RED='\033[0;31m'; CYAN='\033[0;36m'; NC='\033[0m'
info()    { echo -e "${GREEN}[INFO]${NC}   $*"; }
warn()    { echo -e "${YELLOW}[WARN]${NC}   $*"; }
error()   { echo -e "${RED}[ERROR]${NC}  $*" >&2; }
step()    { echo -e "\n${CYAN}>>> $*${NC}"; }

M2_DIR="${HOME}/.m2"

# ---------------------------------------------------------------------------
prompt_nexus_host() {
  if [[ -z "$NEXUS_HOST" ]]; then
    read -rp "IP o hostname del servidor Nexus: " NEXUS_HOST
  fi
  if [[ -z "$NEXUS_HOST" ]]; then
    error "Se requiere la dirección del servidor Nexus."
    exit 1
  fi
}

# ---------------------------------------------------------------------------
check_maven_installed() {
  if ! command -v mvn &>/dev/null; then
    warn "Maven no está instalado o no está en el PATH."
    warn "Descargar desde: https://maven.apache.org/download.cgi"
    read -rp "¿Continuar de todas formas? (s/N): " cont
    [[ "$cont" != "s" && "$cont" != "S" ]] && exit 1
  else
    local mvn_version
    mvn_version=$(mvn -version 2>&1 | head -1)
    info "Maven detectado: ${mvn_version}"
  fi
}

# ---------------------------------------------------------------------------
check_java_installed() {
  if ! command -v java &>/dev/null; then
    warn "Java no está en el PATH."
  else
    local java_version
    java_version=$(java -version 2>&1 | head -1)
    info "Java detectado: ${java_version}"
  fi
}

# ---------------------------------------------------------------------------
backup_existing_settings() {
  local settings_file="${M2_DIR}/settings.xml"
  if [[ -f "$settings_file" ]]; then
    local backup="${settings_file}.backup-$(date +%Y%m%d-%H%M%S)"
    warn "settings.xml existente encontrado. Creando backup: ${backup}"
    cp "$settings_file" "$backup"
  fi
}

# ---------------------------------------------------------------------------
install_settings_xml() {
  local source_template="$1"
  local dest="${M2_DIR}/settings.xml"

  mkdir -p "$M2_DIR"
  cp "$source_template" "$dest"

  # Reemplazar placeholders con los valores reales
  if [[ "$(uname)" == "Darwin" ]]; then
    # macOS sed requiere backup explícito
    sed -i '' "s|NEXUS_HOST|${NEXUS_HOST}|g" "$dest"
    sed -i '' "s|NEXUS_PORT|${NEXUS_PORT}|g" "$dest"
  else
    sed -i "s|NEXUS_HOST|${NEXUS_HOST}|g" "$dest"
    sed -i "s|NEXUS_PORT|${NEXUS_PORT}|g" "$dest"
  fi

  info "settings.xml instalado en: ${dest}"
}

# ---------------------------------------------------------------------------
setup_credentials_env() {
  local shell_rc=""
  if [[ -f "${HOME}/.bashrc" ]]; then
    shell_rc="${HOME}/.bashrc"
  elif [[ -f "${HOME}/.zshrc" ]]; then
    shell_rc="${HOME}/.zshrc"
  fi

  if [[ -n "$shell_rc" ]]; then
    # Verificar si ya están configuradas las variables
    if grep -q "NEXUS_USER" "$shell_rc" 2>/dev/null; then
      warn "Variables NEXUS_USER/NEXUS_PASS ya existen en ${shell_rc}. Omitiendo."
      return
    fi

    echo "" >> "$shell_rc"
    echo "# Credenciales Nexus - Infraestructura CI/CD" >> "$shell_rc"
    echo "# Reemplazar con las credenciales reales proporcionadas por el equipo" >> "$shell_rc"
    echo "export NEXUS_USER=\"\"" >> "$shell_rc"
    echo "export NEXUS_PASS=\"\"" >> "$shell_rc"

    warn "Se agregaron variables NEXUS_USER y NEXUS_PASS en ${shell_rc}"
    warn "Editar ese archivo y completar las credenciales."
  else
    warn "No se encontró .bashrc o .zshrc. Configurar manualmente:"
    warn "  export NEXUS_USER=<usuario>"
    warn "  export NEXUS_PASS=<password>"
  fi
}

# ---------------------------------------------------------------------------
verify_nexus_connection() {
  step "Verificando conexión con Nexus..."
  if command -v curl &>/dev/null; then
    local status
    status=$(curl -s -o /dev/null -w "%{http_code}" \
      "http://${NEXUS_HOST}:${NEXUS_PORT}/service/rest/v1/status" 2>/dev/null || echo "000")
    if [[ "$status" == "200" ]]; then
      info "Nexus accesible en http://${NEXUS_HOST}:${NEXUS_PORT}"
    else
      warn "No se pudo conectar a Nexus (HTTP ${status}). Verificar que esté activo."
      warn "El settings.xml fue instalado igualmente."
    fi
  else
    warn "curl no disponible. Verificar manualmente: http://${NEXUS_HOST}:${NEXUS_PORT}"
  fi
}

# ---------------------------------------------------------------------------
verify_maven_uses_nexus() {
  step "Verificando que Maven resuelve dependencias desde Nexus..."
  if command -v mvn &>/dev/null; then
    local effective_settings
    effective_settings=$(mvn help:effective-settings 2>/dev/null | grep -A3 "mirror" | head -10 || true)
    if echo "$effective_settings" | grep -q "$NEXUS_HOST"; then
      info "Maven configurado correctamente para usar Nexus."
    else
      warn "No se pudo confirmar la configuración. Ejecutar manualmente:"
      warn "  mvn help:effective-settings | grep mirror"
    fi
  fi
}

# ---------------------------------------------------------------------------
print_summary() {
  echo ""
  echo "==========================================================="
  echo "  Configuración completada"
  echo "==========================================================="
  echo ""
  echo "  Nexus URL       : http://${NEXUS_HOST}:${NEXUS_PORT}"
  echo "  settings.xml    : ${M2_DIR}/settings.xml"
  echo "  Tipo proyecto   : ${PROJECT_TYPE}"
  echo ""
  echo "  Repositorios configurados:"
  echo "    Releases  → http://${NEXUS_HOST}:${NEXUS_PORT}/repository/maven-releases/"
  echo "    Snapshots → http://${NEXUS_HOST}:${NEXUS_PORT}/repository/maven-snapshots/"
  if [[ "$PROJECT_TYPE" == "mule" || "$PROJECT_TYPE" == "ambos" ]]; then
    echo "    Mule      → http://${NEXUS_HOST}:${NEXUS_PORT}/repository/mule-releases/"
  fi
  echo ""
  echo "  PRÓXIMOS PASOS:"
  echo "    1. Completar credenciales en ~/.bashrc: NEXUS_USER y NEXUS_PASS"
  echo "    2. Recargar shell: source ~/.bashrc"
  echo "    3. Probar compilación: mvn clean install"
  echo "    4. Para subir artefactos existentes: ver scripts/migration/"
  echo "==========================================================="
}

# ---------------------------------------------------------------------------
main() {
  echo "==========================================================="
  echo "  Configuración de entorno de desarrollo"
  echo "  Plataforma: $(uname -s)"
  echo "==========================================================="

  prompt_nexus_host
  check_maven_installed
  check_java_installed

  step "Instalando settings.xml..."
  backup_existing_settings

  local template_source
  case "$PROJECT_TYPE" in
    mule)
      template_source="${TEMPLATES_DIR}/mule/settings.xml"
      ;;
    ambos)
      # Para ambos: usar el settings de Java que incluye perfil Mule
      template_source="${TEMPLATES_DIR}/java-maven/settings.xml"
      ;;
    *)
      template_source="${TEMPLATES_DIR}/java-maven/settings.xml"
      ;;
  esac

  if [[ ! -f "$template_source" ]]; then
    error "Template no encontrado: ${template_source}"
    exit 1
  fi

  install_settings_xml "$template_source"
  setup_credentials_env
  verify_nexus_connection
  verify_maven_uses_nexus
  print_summary
}

main "$@"
