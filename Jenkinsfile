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
    }

    environment {
        WORKSPACE           = "${env.WORKSPACE}"
        NEXUS_CREDENTIAL_ID = 'Nexus-Credential'
        NEXUS_URL           = "nexus:8081"
        NEXUS_REPOSITORY    = "maven-project-releases"
        
        // Optimización de memoria para Maven en tu contenedor
        MAVEN_OPTS          = '-Xmx1024m -XX:+TieredCompilation -XX:TieredStopAtLevel=1'
        APP_NAME            = "ServiciosSTD_WS"
    }

    tools {
        // Enlazamos las herramientas que configuramos en tu Jenkins Web
        maven 'localMaven'
        jdk 'localJdk8'
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '20', artifactNumToKeepStr: '5'))
        timeout(time: 45, unit: 'MINUTES')
        timestamps()
        disableConcurrentBuilds()
    }

    stages {

        stage('Escaneo de Secretos') {
            steps {
                script {
                    echo "=== Verificando que no hay datos sensibles en el código Java ==="
                    // Filtro estricto sobre tu subcarpeta real
                    if grep -rn --include="*.java" --include="*.properties" --include="*.xml" \
                       -E "(password|passwd|secret|api_key|apikey)\s*=\s*['\"][^$\{][^\'\"]{3,}" \
                       serviciosstd_ws/src/ 2>/dev/null; then
                        echo "🛑 ERROR: Posibles credenciales hardcodeadas detectadas en el codigo de QA."
                        exit 1
                    fi
                    echo "✅ Sin secretos detectados."
                }
            }
        }

        stage('Build & Compile') {
            steps {
                echo '=== Compilando codigo fuente base ==='
                sh 'mvn clean compile -f serviciosstd_ws/pom.xml -DskipTests=true -B -q'
            }
        }

        stage('Tests Unitarios y Cobertura') {
            when { expression { !params.SKIP_TESTS } }
            steps {
                echo '=== Ejecutando JUnit Tests reales ==='
                sh 'mvn test -f serviciosstd_ws/pom.xml -B'
            }
            post {
                always {
                    // Publica los reportes XML en la interfaz de Jenkins
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
                        sh 'mvn org.owasp:dependency-check-maven:check -f serviciosstd_ws/pom.xml -DfailBuildOnCVSS=7 -Dformat=HTML -B -q || true'
                    }
                }
                stage('SpotBugs') {
                    steps {
                        echo '=== Analizando Bugs Estáticos en Código ==='
                        sh 'mvn com.github.spotbugs:spotbugs-maven-plugin:spotbugs -f serviciosstd_ws/pom.xml -B -q || true'
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
                echo '=== Generando empaquetado final ServiciosSTD_WS.war ==='
                sh 'mvn package -f serviciosstd_ws/pom.xml -DskipTests=true -B -q'
            }
        }

        stage('Publicar en Nexus') {
            steps {
                echo '=== Subiendo artefacto consolidado a Sonatype Nexus ==='
                nexusArtifactUploader(
                  nexusVersion: 'nexus3',
                  protocol: 'http',
                  nexusUrl: "${NEXUS_URL}",
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
            
            echo '=== Limpiando espacio de trabajo local ==='
            cleanWs(cleanWhenSuccess: true, cleanWhenFailure: false, cleanWhenAborted: true)
        }
    }
}