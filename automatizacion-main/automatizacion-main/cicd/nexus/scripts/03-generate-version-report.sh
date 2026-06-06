#!/usr/bin/env bash
# =============================================================================
# 03-generate-version-report.sh
# Genera reporte de todas las versiones publicadas en Nexus
# Uso: ./03-generate-version-report.sh [NEXUS_URL] [USER] [PASS] [output.txt]
# =============================================================================
set -euo pipefail

NEXUS_URL="${1:-http://localhost:8081}"
NEXUS_USER="${2:-admin}"
NEXUS_PASS="${3:-admin123}"
OUTPUT_FILE="${4:-version-report-$(date +%Y%m%d-%H%M%S).txt}"
API="${NEXUS_URL}/service/rest/v1"

REPOS=("maven-releases" "maven-snapshots" "mule-releases" "mule-snapshots" "legacy-artifacts")

{
echo "====================================================================="
echo "  REPORTE DE VERSIONES - Nexus Repository Manager"
echo "  Generado: $(date '+%Y-%m-%d %H:%M:%S')"
echo "  Servidor: ${NEXUS_URL}"
echo "====================================================================="
echo ""

for repo in "${REPOS[@]}"; do
  echo "---------------------------------------------------------------------"
  echo "  Repositorio: ${repo}"
  echo "---------------------------------------------------------------------"

  token=""
  total=0
  while :; do
    local_url="${API}/components?repository=${repo}"
    [[ -n "$token" ]] && local_url="${local_url}&continuationToken=${token}"

    response=$(curl -s -f -u "${NEXUS_USER}:${NEXUS_PASS}" "$local_url" 2>/dev/null || echo '{"items":[],"continuationToken":null}')

    items=$(echo "$response" | python3 -c "
import sys, json
data = json.load(sys.stdin)
for item in data.get('items', []):
    g  = item.get('group','') or '-'
    a  = item.get('name','')
    v  = item.get('version','')
    fmt= item.get('format','')
    print(f'  {g}:{a}:{v}  [{fmt}]')
" 2>/dev/null || true)

    if [[ -n "$items" ]]; then
      echo "$items"
      count=$(echo "$items" | wc -l)
      total=$((total + count))
    fi

    token=$(echo "$response" | python3 -c "import sys,json; d=json.load(sys.stdin); print(d.get('continuationToken') or '')" 2>/dev/null || true)
    [[ -z "$token" ]] && break
  done

  echo ""
  echo "  Total artefactos en ${repo}: ${total}"
  echo ""
done

echo "====================================================================="
echo "  FIN DEL REPORTE"
echo "====================================================================="
} | tee "$OUTPUT_FILE"

echo ""
echo "Reporte guardado en: ${OUTPUT_FILE}"
