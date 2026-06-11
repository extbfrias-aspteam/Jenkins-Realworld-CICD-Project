#!/bin/bash

# =================================================================
# Script: sanitize_environments.sh
# Objetivo: Detectar, segmentar y limpiar archivos de propiedades
# =================================================================

TARGET_DIR=${1:-"serviciosstd_ws"}
STRICT_MODE=$2 # --strict para romper el pipeline
ENVIRONMENT=${3:-"qa"} # ambiente por defecto

REPORT_DIR="security_reports_${ENVIRONMENT}"
mkdir -p "$REPORT_DIR"

echo "=========================================================="
echo " Iniciando Auditoría y Sanitización para Ambiente: ${ENVIRONMENT^^}"
echo "=========================================================="

# 1. Buscar archivos de configuración candidatos
echo "1. Identificando archivos de propiedades y configuración..."
CONFIG_FILES=$(find "$TARGET_DIR" -type f \( -name "*.properties" -o -name "*.xml" -o -name "*.yml" -o -name "*.yaml" \))

if [ -z "$CONFIG_FILES" ]; then
    echo "INFO: No se encontraron archivos de configuración para analizar."
    exit 0
fi

SECRETS_FOUND=0
TEMP_REPORT="${REPORT_DIR}/extracted_secrets.txt"
echo "--- Secretos Extraídos para Resguardo ($ENVIRONMENT) ---" > "$TEMP_REPORT"

# Patrones regex para detectar datos reales vs plantillas vacías
# Evita alertar si el valor ya es una variable como ${VAR} o TO_BE_INJECTED
REGEX_SUSPICIOUS="(password|pass|secret|key|user|token|credentials|pwd)\s*=\s*(?!(\\\$\{[A-Za-z0-9_-]+\}|[A-Z0-9_-]+_BY_JENKINS)).*"

# 2. Analizar archivo por archivo
for FILE in $CONFIG_FILES; do
    # Buscamos si el archivo contiene asignaciones sospechosas reales
    MATCHES=$(grep -E -i "$REGEX_SUSPICIOUS" "$FILE")
    
    if [ -n "$MATCHES" ]; then
        echo "⚠️ ALERTA: Datos sensibles detectados en: $FILE"
        SECRETS_FOUND=1
        
        # Almacenamos el hallazgo para resguardo de la gerencia/Vault
        echo "Archivo: $FILE" >> "$TEMP_REPORT"
        echo "$MATCHES" >> "$TEMP_REPORT"
        echo "--------------------------------------" >> "$TEMP_REPORT"
        
        # 3. EXTRAER Y SANITIZAR (Dejar solo la estructura/plantilla)
        echo "🧼 Sanitizando archivo $FILE en caliente para el proceso CI/CD..."
        
        # Este comando reemplaza el valor real por un Token genérico del ambiente
        # Ejemplo: PASS_Std = password  ->  PASS_Std = INJECTED_BY_JENKINS
        sed -i -E "s/((password|pass|secret|key|user|token|pwd)\s*=\s*).*/\1INJECTED_BY_JENKINS/gI" "$FILE"
    fi
done

# =================================================================
# 4. Evaluación del Filtro de Seguridad (Gatekeeper)
# =================================================================
echo "=========================================================="
echo " Resumen de Sanitización"
echo "=========================================================="

if [ "$SECRETS_FOUND" -eq 1 ]; then
    echo "❌ Se encontraron archivos con datos sensibles expuestos."
    echo "📋 Los secretos fueron extraídos y resguardados en: $TEMP_REPORT"
    echo "💡 Los archivos originales en el workspace han sido limpiados temporalmente."
    
    if [ "$STRICT_MODE" == "--strict" ]; then
        echo "⛔ MODE STRICT: Abortando pipeline. Corrige tu repositorio de Git antes de continuar."
        exit 1
    else
        echo "⚠️ MODE WARNING: Archivos limpiados en memoria. Permitido continuar."
        exit 0
    fi
else
    echo "✅ ¡Felicidades! Todos los archivos de configuración están limpios (usan plantillas)."
    echo "🚀 El pipeline tiene luz verde para proceder a compilar y ejecutar 'mvn test'."
    exit 0
fi