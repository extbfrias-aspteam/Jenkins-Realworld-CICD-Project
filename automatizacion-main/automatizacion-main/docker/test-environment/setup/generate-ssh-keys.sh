#!/bin/bash
# ─────────────────────────────────────────────────────────────────
# Genera el par de claves SSH para que Jenkins acceda a los
# servers destino. Debe ejecutarse ANTES de docker-compose build.
# ─────────────────────────────────────────────────────────────────
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
KEYS_DIR="${SCRIPT_DIR}/ssh-keys"

echo "==> Generando claves SSH para Jenkins..."

mkdir -p "${KEYS_DIR}"

# Generar par de claves si no existe
if [ ! -f "${KEYS_DIR}/jenkins_rsa" ]; then
    ssh-keygen -t rsa -b 4096 \
        -C "jenkins-test-env" \
        -f "${KEYS_DIR}/jenkins_rsa" \
        -N ""
    echo "    Claves generadas en ${KEYS_DIR}/"
else
    echo "    Claves ya existen, omitiendo generación"
fi

chmod 600 "${KEYS_DIR}/jenkins_rsa"
chmod 644 "${KEYS_DIR}/jenkins_rsa.pub"

echo "==> Clave pública:"
cat "${KEYS_DIR}/jenkins_rsa.pub"
echo ""
echo "==> Listo. La clave pública será copiada a los servers destino al buildear las imágenes."
