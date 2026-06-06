#!/bin/bash
set -e

echo "[$(hostname)] Iniciando servidor destino..."

# Regenerar host keys si no existen
if [ ! -f /etc/ssh/ssh_host_rsa_key ]; then
    ssh-keygen -A
fi

echo "[$(hostname)] Servidor SSH listo en puerto 22"
exec /usr/sbin/sshd -D
