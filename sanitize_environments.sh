#!/bin/bash

# =================================================================
# Script: sanitize_environments.sh
# Objetivo: Detectar, extraer y limpiar credenciales en archivos de config
# Compatible con: Git Bash (Windows) y Linux (Docker/Jenkins)
# =================================================================

TARGET_DIR=${1:-"serviciosstd_ws"}
STRICT_MODE=$2 # --strict para romper el pipeline si encuentra algo
ENVIRONMENT=${3:-"qa"} # ambiente por defecto

REPORT_DIR="security_reports_${ENVIRONMENT}"
mkdir -p "$REPORT_DIR"

echo "=========================================================="
echo " Iniciando Auditoría y Sanitización para Ambiente: ${ENVIRONMENT^^}"
echo "=========================================================="

# 1. Buscar archivos de configuración candidatos (Compatibilidad Universal)
echo "1. Identificando archivos de propiedades y configuración..."
CONFIG_FILES=$(find "$TARGET_DIR" -type f -name "*.properties" && find "$TARGET_DIR" -type f -name "*.xml")

if [ -z "$CONFIG_FILES" ]; then
    echo "⚠️ ERROR: No se encontraron archivos para analizar en la ruta '$TARGET_DIR'."
    exit 1
fi

SECRETS_FOUND=0
TEMP_REPORT="${REPORT_DIR}/extracted_secrets.txt"
echo "--- Secretos Extraídos para Resguardo ($ENVIRONMENT) ---" > "$TEMP_REPORT"

# Expresión regular limpia: Busca claves comunes seguidas de un '=' y texto plano real
REGEX_SUSPICIOUS="(password|pass|secret|key|user|token|credentials|pwd)\s*=\s*[^$\s]+"

# 2. Analizar archivo por archivo
for FILE in $CONFIG_FILES; do
    # Filtramos para ignorar si el valor ya está tokenizado (INJECTED_BY_JENKINS, etc.)
    MATCHES=$(grep -E -i "$REGEX_SUSPICIOUS" "$FILE" | grep -v -E "INJECTED_BY_JENKINS|VALOR_SEGURO|\{")
    
    if [ -n "$MATCHES" ]; then
        echo "⚠️ ALERTA: Datos sensibles detectados en: $FILE"
        SECRETS_FOUND=1
        
        # Guardamos el hallazgo en el reporte de la gerencia antes de borrarlo
        echo "Archivo: $FILE" >> "$TEMP_REPORT"
        echo "$MATCHES" >> "$TEMP_REPORT"
        echo "--------------------------------------" >> "$TEMP_REPORT"
        
        # 3. ACCIÓN EN CALIENTE: "Limpiar" el archivo original
        # Reemplaza el valor real (ej: 'password') por 'INJECTED_BY_JENKINS'
        sed -i -E "s/((password|pass|secret|key|user|token|pwd)\s*=\s*).*/\1INJECTED_BY_JENKINS/gI" "$FILE"
    fi
done

# =================================================================
# 4. Resumen y Decisiones del Pipeline
# =================================================================
echo "=========================================================="
echo " Resumen de Sanitización"
echo "=========================================================="

if [ "$SECRETS_FOUND" -eq 1 ]; then
    echo "❌ Se encontraron archivos con datos sensibles expuestos."
    echo "📋 Los secretos fueron extraídos y resguardados en: $TEMP_REPORT"
    echo "🧼 Los archivos en el espacio de trabajo han sido LIMPIADOS (ahora son plantillas)."
    
    if [ "$STRICT_MODE" == "--strict" ]; then
        echo "⛔ MODO ESTRICTO: Abortando pipeline para proteger la infraestructura."
        exit 1
    else
        echo "⚠️ MODO ADVERTENCIA: Archivos sanitizados en memoria. Permitido continuar."
        exit 0
    fi
else
    echo "✅ ¡Felicidades! Todos los archivos de configuración están limpios (usan plantillas)."
    echo "🚀 El pipeline tiene luz verde para proceder a compilar y ejecutar 'mvn test'."
    exit 0
fi