// TEST
def COLOR_MAP = [
    'SUCCESS': 'good', 
    'FAILURE': 'danger',
    'UNSTABLE': 'danger'
]
pipeline {
    agent any
    environment {
        NEXUS_CREDENTIAL_ID = 'Nexus-Credential'
        NEXUS_USER = "$NEXUS_CREDS_USR"
        NEXUS_PASSWORD = "$NEXUS_CREDS_PSWD"
        NEXUS_URL = "nexus:8081"
        NEXUS_REPOSITORY = "maven_project"
        NEXUS_REPO_ID    = "maven_project"
        ARTVERSION = "${env.BUILD_ID}"
    }
    tools {
        maven 'localMaven'
        jdk 'localJdk8'
    }
    
    stages {
        stage('Build') {
            steps {
                dir('serviciosstd_ws') {
                    // Forzamos a Maven a ignorar la validación estricta de SSL
                    sh 'mvn clean package -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true'
                }
            }
            post {
                success {
                    echo ' now Archiving '
                    // Busca los entregables dentro de la subcarpeta de forma correcta
                    archiveArtifacts artifacts: 'serviciosstd_ws/target/*.war'
                }
            }
        }
        
        stage('Unit Test'){
            steps {
                dir('serviciosstd_ws') {
                    sh 'mvn test'
                }
            }
        }
        
        stage('Integration Test'){
            steps {
                dir('serviciosstd_ws') {
                    sh 'mvn verify -DskipUnitTests'
                }
            }
        }
        
        stage ('Checkstyle Code Analysis'){
            steps {
                dir('serviciosstd_ws') {
                    sh 'mvn checkstyle:checkstyle'
                }
            }
            post {
                success {
                    echo 'Generated Analysis Result'
                }
            }
        }
        
        stage('SonarQube Inspection') {
            steps {
                dir('serviciosstd_ws') {
                    withSonarQubeEnv('SonarQube') { 
                        withCredentials([string(credentialsId: 'SonarQube-Token', variable: 'SONAR_TOKEN')]) {
                            sh """
                            mvn clean verify sonar:sonar \
                            -Dsonar.projectKey=ASP-POC \
                            -Dsonar.host.url=http://sonarqube:9000 \
                            -Dsonar.scm.provider=git \
                            -Dsonar.login=$SONAR_TOKEN
                            """
                        }
                    }
                }
            }
        }
        
        stage('SonarQube Quality Gate') {
            steps {
                timeout(time: 1, unit: 'HOURS') {
                    script {
                        // Espera el Webhook de SonarQube y aborta si no pasa las métricas
                        def qg = waitForQualityGate()
                        if (qg.status != 'OK') {
                            currentBuild.result = 'FAILURE'
                            error "Pipeline abortado: El código no pasó el Quality Gate de SonarQube. Estado: ${qg.status}"
                        }
                    }
                }
            }
        }
        
        stage("Nexus Artifact Uploader"){
            steps {
                dir('serviciosstd_ws') {
                    script {
                        // Encontrar el nombre exacto del archivo .war generado dinámicamente en target/
                        def warFiles = findFiles(glob: 'target/*.war')
                        if (warFiles.length == 0) {
                            error "No se encontró ningún archivo .war en target/"
                        }
                        def warPath = warFiles[0].path
                        
                        nexusArtifactUploader(
                            nexusVersion: 'nexus3',
                            protocol: 'http',
                            nexusUrl: "${NEXUS_URL}",
                            groupId: 'webapp',
                            version: "${env.BUILD_ID}-${env.BUILD_TIMESTAMP}",
                            repository: 'maven-releases',
                            credentialsId: "${NEXUS_CREDENTIAL_ID}",
                            artifacts: [
                                [artifactId: 'webapp',
                                classifier: '',
                                file: "${warPath}", 
                                type: 'war']
                            ]
                        )
                    }
                }
            }
        }
    }
    
    post {
        always {
            echo 'Slack Notifications.'
            slackSend channel: '#prestigious-channel',
            color: COLOR_MAP[currentBuild.currentResult],
            message: "*${currentBuild.currentResult}:* Job Name '${env.JOB_NAME}' build ${env.BUILD_NUMBER} \n Build Timestamp: ${env.BUILD_TIMESTAMP} \n Project Workspace: ${env.WORKSPACE} \n More info at: ${env.BUILD_URL}"
        }
    }
}