/**
 * secretScan.groovy - Shared Library
 * Escanea el código fuente en busca de credenciales y datos sensibles.
 * Se debe llamar SIEMPRE como primer step del pipeline.
 *
 * Uso:
 *   @Library('cicd-shared-library') _
 *   secretScan()                        // escaneo estricto (falla el build)
 *   secretScan(strict: false)           // solo advertencia
 *   secretScan(paths: ['src/', 'conf/'])
 */
def call(Map config = [:]) {
    def strict  = config.strict  != null ? config.strict : true
    def paths   = config.paths   ?: ['src/', '.']

    echo "[secretScan] Iniciando escaneo de secretos (strict=${strict})..."

    def scanResult = sh(
        script: """
            FOUND=0
            for PATH_TO_SCAN in ${paths.join(' ')}; do
                # Contraseñas hardcodeadas
                if grep -rn --include="*.java" --include="*.xml" \
                   --include="*.properties" --include="*.yml" --include="*.yaml" \
                   -E '(password|passwd|secret|api_key|apikey|token)\\s*[=:]\\s*["'"'"'][^\\$\\{][^"'"'"']{3,}' \
                   "\$PATH_TO_SCAN" 2>/dev/null; then
                    FOUND=1
                fi
                # Claves privadas
                if grep -rn --include="*.java" --include="*.xml" --include="*.properties" \
                   "BEGIN PRIVATE KEY\\|BEGIN RSA PRIVATE KEY\\|BEGIN EC PRIVATE KEY" \
                   "\$PATH_TO_SCAN" 2>/dev/null; then
                    FOUND=1
                fi
                # IPs hardcodeadas (fuera de comentarios y test)
                if grep -rn --include="*.java" --include="*.xml" \
                   -E '"[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}"' \
                   "\$PATH_TO_SCAN" 2>/dev/null | grep -v "//\\|test\\|Test\\|127\\.0\\.0"; then
                    FOUND=1
                fi
            done
            exit \$FOUND
        """,
        returnStatus: true
    )

    if (scanResult != 0) {
        if (strict) {
            error("[secretScan] BLOQUEADO: Se encontraron posibles secretos en el código. Usar variables de entorno o Vault.")
        } else {
            unstable("[secretScan] ADVERTENCIA: Posibles secretos detectados. Revisar antes de merge a main.")
        }
    } else {
        echo "[secretScan] OK: Sin secretos detectados."
    }
}
