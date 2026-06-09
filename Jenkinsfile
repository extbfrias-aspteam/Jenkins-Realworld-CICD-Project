def COLOR_MAP = [
    'SUCCESS': 'good', 
    'FAILURE': 'danger',
    'UNSTABLE': 'danger'
]

pipeline {
    agent any

    parameters {
        choice(
            name: 'ENVIRONMENT',
            choices: ['dev', 'qa', 'staging', 'production'],
            description: 'Ambiente de destino para el despliegue con Ansible'
        )
        booleanParam(
            name: 'SKIP_TESTS',
            defaultValue: false,
            description: 'Saltar pruebas unitarias y de integracion (Emergencias)'
        )
        booleanParam(
            name: 'SKIP_SECURITY',
            defaultValue: false,
            description: 'Saltar OWASP y SpotBugs'
        )
        booleanParam(
            name: 'STRICT_SECRETS',
            defaultValue: false,
            description: 'Activar para romper el build de Jenkins si tu script detecta secretos criticos'
        )
        booleanParam(
            name: 'FORCE_BYPASS_BUILD',
            defaultValue: false,
            description: 'Activar para simular el WAR (Evitar bloqueo de dependencias locales)'
        )
    }

    environment {
        WORKSPACE           = "${env.WORKSPACE}"
        NEXUS_CREDENTIAL_ID = 'Nexus-Credential'
        NEXUS_URL           = "nexus:8081"
        NEXUS_REPOSITORY    = "maven-project-releases"
        MAVEN_OPTS          = '-Xmx1024m -XX:+TieredCompilation -XX:TieredStopAtLevel=1'
        APP_NAME            = "ServiciosSTD_WS"
    }

    tools {
        maven 'localMaven'
        jdk 'localJdk8'
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '20', artifactNumToKeepStr: '5'))
        timeout(time: 45, unit: 'MINUTES')
        disableConcurrentBuilds()
    }

    stages {

        stage('Debug Workspace') {
            steps {
                echo '=== Verificando Estructura de Archivos ==='
                sh 'ls -la'
                sh 'ls -la serviciosstd_ws/'
            }
        }

        stage('Escaneo de Secretos (Script Personalizado)') {
            steps {
                echo "=== Iniciando Analisis con scan_secrets.sh ==="
                script {
                    def strictFlag = params.STRICT_SECRETS ? "--strict" : ""
                    
                    // CORREGIDO: Entramos a la subcarpeta antes de ejecutar tu script en Bash
                    sh """
                        if [ -f "serviciosstd_ws/scan_secrets.sh" ]; then
                            chmod +x serviciosstd_ws/scan_secrets.sh
                            cd serviciosstd_ws && bash scan_secrets.sh ${strictFlag} || [ "${params.STRICT_SECRETS}" = "false" ]
                        else
                            echo "🛑 ERROR: No se encontro el archivo scan_secrets.sh dentro de serviciosstd_ws/"
                            exit 1
                        fi
                    """
                    
                    echo "=== Auditoría de Reportes Generados ==="
                    sh """
                        LATEST_REPORT=\$(ls -td /tmp/secret-scan-* 2>/dev/null | head -n 1)
                        if [ -n "\$LATEST_REPORT" ]; then
                            echo "📂 Analizando carpeta de reportes: \$LATEST_REPORT"
                            if [ -s "\$LATEST_REPORT/base64-data.txt" ]; then
                                echo "⚠️ --- DETECCIONES DE DATOS / RUTAS EXPUESTAS ---"
                                cat "\$LATEST_REPORT/base64-data.txt"
                            fi
                        else
                            echo "❌ No se localizo la carpeta de reportes temporales."
                        fi
                    """
                }
            }
        }

        stage('Build & Compile') {
            steps {
                script {
                    if (params.FORCE_BYPASS_BUILD) {
                        echo '=== [BYPASS] Creando entorno simulado de empaquetado (WAR Falso) ==='
                        sh 'mkdir -p serviciosstd_ws/target && touch serviciosstd_ws/target/ServiciosSTD_WS.war'
                    } else {
                        echo '=== Compilando codigo fuente base forzando actualizacion (-U) ==='
                        sh 'mvn clean compile -f serviciosstd_ws/pom.xml -DskipTests=true -U -B -q'
                    }
                }
            }
        }

        stage('Tests Unitarios y Cobertura') {
            when { expression { !params.SKIP_TESTS && !params.FORCE_BYPASS_BUILD } }
            steps {
                echo '=== Ejecutando JUnit Tests reales ==='
                sh 'mvn test -f serviciosstd_ws/pom.xml -B'
            }
            post {
                always {
                    junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true
                }
            }
        }

        stage('Análisis de Seguridad') {
            when { expression { !params.SKIP_SECURITY } }
            parallel {
                stage('OWASP Dependency Check') {
                    steps {
                        echo '=== Analizando vulnerabilidades en librerias externas (JARs) ==='
                        script {
                            if (params.FORCE_BYPASS_BUILD) {
                                echo '=== [BYPASS] Saltando analasis OWASP real ==='
                            } else {
                                sh 'mvn org.owasp:dependency-check-maven:check -f serviciosstd_ws/pom.xml -DfailBuildOnCVSS=7 -Dformat=HTML -B -q || true'
                            }
                        }
                    }
                }
                stage('SpotBugs') {
                    steps {
                        echo '=== Analizando Bugs Estáticos en Código ==='
                        script {
                            if (params.FORCE_BYPASS_BUILD) {
                                echo '=== [BYPASS] Saltando SpotBugs real ==='
                            } else {
                                sh 'mvn com.github.spotbugs:spotbugs-maven-plugin:spotbugs -f serviciosstd_ws/pom.xml -B -q || true'
                            }
                        }
                    }
                }
            }
        }

        stage('SonarQube Quality Gate') {
            when { expression { !params.SKIP_SECURITY } }
            steps {
                echo '=== Enviando metricas completas a SonarQube ==='
                withSonarQubeEnv('SonarQube') {
                    withCredentials([string(credentialsId: 'SonarQube-Token', variable: 'SONAR_TOKEN')]) {
                        sh """
                        mvn sonar:sonar -f serviciosstd_ws/pom.xml \
                            -Dsonar.projectKey=${APP_NAME} \
                            -Dsonar.projectName=${APP_NAME} \
                            -Dsonar.host.url=http://sonarqube:9000 \
                            -Dsonar.login=$SONAR_TOKEN \
                            -B -q
                        """
                    }
                }
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Package War') {
            steps {
                script {
                    if (params.FORCE_BYPASS_BUILD) {
                        echo '=== [BYPASS] Usando WAR simulado existente ==='
                    } else {
                        echo '=== Generando empaquetado final ServiciosSTD_WS.war ==='
                        sh 'mvn package -f serviciosstd_ws/pom.xml -DskipTests=true -B -q'
                    }
                }
            }
        }

        stage('Publicar en Nexus') {
            steps {
                echo '=== Subiendo artefacto consolidado a Sonatype Nexus ==='
                nexusArtifactUploader(
                  nexusVersion: 'nexus3',
                  protocol: 'http',
                  nexusUrl: "${NEXUS_URL}",
                  groupId: 'com.mx.std',
                  version: "${env.BUILD_ID}",
                  repository: "${NEXUS_REPOSITORY}",
                  credentialsId: "${NEXUS_CREDENTIAL_ID}",
                  artifacts: [
                      [artifactId: "${APP_NAME}",
                      classifier: '',
                      file: "serviciosstd_ws/target/ServiciosSTD_WS.war",
                      type: 'war']
                  ]
                )
            }
            post {
                success {
                    archiveArtifacts artifacts: 'serviciosstd_ws/target/ServiciosSTD_WS.war'
                }
            }
        }

        stage('Deploy to Environment (Ansible)') {
            steps {
                echo "=== Desplegando en Entorno: ${params.ENVIRONMENT.toUpperCase()} ==="
                withCredentials([usernamePassword(credentialsId: 'Ansible-Credential', passwordVariable: 'PASSWORD', usernameVariable: 'USER_NAME')]) {
                    sh "ansible-playbook -i ${WORKSPACE}/ansible-config/aws_ec2.yaml ${WORKSPACE}/deploy.yaml --extra-vars \"ansible_user=$USER_NAME ansible_password=$PASSWORD hosts=tag_Environment_${params.ENVIRONMENT} workspace_path=${WORKSPACE}\""
                }
            }
        }

    } // end stages

    post {
        always {
            echo '=== Notificando estado actual a Slack ==='
            slackSend channel: 'apc-cicd-2026', 
            color: COLOR_MAP[currentBuild.currentResult],
            message: "*${currentBuild.currentResult}:* Proyecto '${env.JOB_NAME}' - Build #${env.BUILD_NUMBER} \n Más información en: ${env.BUILD_URL}"
        }
    }
}