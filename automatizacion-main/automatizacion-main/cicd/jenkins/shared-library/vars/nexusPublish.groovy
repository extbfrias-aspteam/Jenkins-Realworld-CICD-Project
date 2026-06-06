/**
 * nexusPublish.groovy - Shared Library
 * Publica un artefacto Maven en Nexus de forma estandarizada.
 *
 * Uso en Jenkinsfile:
 *   @Library('cicd-shared-library') _
 *   nexusPublish(
 *     nexusUrl: 'http://nexus:8081',
 *     credentialsId: 'nexus-credentials',
 *     version: '1.2.3',
 *     artifactPattern: 'target/*.jar'
 *   )
 */
def call(Map config = [:]) {
    def nexusUrl      = config.nexusUrl      ?: error('nexusPublish: nexusUrl requerido')
    def credId        = config.credentialsId ?: 'nexus-credentials'
    def version       = config.version       ?: error('nexusPublish: version requerido')
    def artifactPat   = config.artifactPattern ?: 'target/*.jar'
    def skipDeploy    = config.skip          ?: false

    if (skipDeploy) {
        echo "[nexusPublish] Publicación omitida por configuración."
        return
    }

    def nexusRepo = version.endsWith('SNAPSHOT') ? 'maven-snapshots' : 'maven-releases'

    withCredentials([usernamePassword(
        credentialsId: credId,
        usernameVariable: 'NEXUS_USER',
        passwordVariable: 'NEXUS_PASS'
    )]) {
        sh """
            mvn deploy -DskipTests=true \
                -DaltDeploymentRepository=nexus-deploy::default::${nexusUrl}/repository/${nexusRepo}/ \
                -B -q
        """
    }

    echo "[nexusPublish] Artefacto publicado en ${nexusUrl}/repository/${nexusRepo}/"
}
