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
        jdk 'JDK-1.8'
    }
    
    stages {
        stage('Build') {
            tools {
                jdk 'JDK-1.8' // Asegura que use la descarga de Adoptium Java 8
            }
            steps {
                dir('ceroahorrows.war/CEROAhorroWS') {
                    configFileProvider([configFile(fileId: 'maven-local-repo', variable: 'MAVEN_SETTINGS')]) {
                        sh 'mvn clean package -s $MAVEN_SETTINGS -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true'
                    }
                }
            }
            post {
                success {
                    echo ' now Archiving '
                    archiveArtifacts artifacts: 'ceroahorrows.war/CEROAhorroWS/target/*.war'
                }
            }
        }
        
        stage('Unit Test'){
            tools {
                jdk 'JDK-1.8' // Asegura que use la descarga de Adoptium Java 8
            }
            steps {
                dir('ceroahorrows.war/CEROAhorroWS') {
                    sh 'mvn test'
                }
            }
        }
        
        stage('Integration Test'){
            tools {
                jdk 'JDK-1.8' // Asegura que use la descarga de Adoptium Java 8
            }
            steps {
                dir('ceroahorrows.war/CEROAhorroWS') {
                    sh 'mvn verify -DskipUnitTests'
                }
            }
        }
        
        stage ('Checkstyle Code Analysis'){
            tools {
                jdk 'JDK-1.8' // Asegura que use la descarga de Adoptium Java 8
            }
            steps {
                dir('ceroahorrows.war/CEROAhorroWS') {
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
            tools {
                // Jenkins usará el instalador automático de JDK 11 configurado para el scanner
                jdk 'jdk11' 
            }
            steps {
                dir('ceroahorrows.war/CEROAhorroWS/') {
                    withSonarQubeEnv('SonarQube') { 
                        withCredentials([string(credentialsId: 'SonarQube-Token', variable: 'SONAR_TOKEN')]) {
                            // CORRECCIÓN: Usamos la variable $SONAR_TOKEN limpia y la URL pública correcta
                            sh """
                            mvn sonar:sonar \
                            -Dsonar.projectKey=extbfrias-aspteam_Jenkins-Realworld-CICD-Project_AZ8Wcm5tMb0D80z0WSTJ" \
                            -Dsonar.host.url=http://sonarqube:9000 \
                            -Dsonar.scm.provider=git \
                            -Dsonar.login=${SONAR_TOKEN}
                            """
                        }
                    }
                }
            }
        }
        stage('SonarQube Analysis') {
            tools {
                jdk 'jdk11' // Asegura que use la descarga de Adoptium Java 11
            }
            steps {
                script {
                    // 1. Declare your variable inside a script block
                    def mvn = tool 'localMaven'
                    
                    // 2. Wrap your maven execution inside the SonarQube environment wrapper
                    withSonarQubeEnv('SonarQube') { // Match the exact name of your server in Jenkins
                        sh "${mvn}/bin/mvn clean verify sonar:sonar -Dsonar.projectKey=extbfrias-aspteam_Jenkins-Realworld-CICD-Project_AZ8Wcm5tMb0D80z0WSTJ"
                    }
                }
            }
        }
        
        stage('SonarQube Quality Gate') {
            steps {
                // Entramos al mismo subdirectorio para encontrar el archivo 'report-task.txt'
                dir('ceroahorrows.war/CEROAhorroWS') {
                    timeout(time: 1, unit: 'HOURS') {
                        script {
                            def qg = waitForQualityGate()
                            if (qg.status != 'OK') {
                                currentBuild.result = 'FAILURE'
                                error "Pipeline abortado: El código no pasó el Quality Gate de SonarQube. Estado: ${qg.status}"
                            }
                        }
                    }
                }
            }
        }
        
        stage("Nexus Artifact Uploader"){
            steps {
                dir('ceroahorrows.war/CEROAhorroWS') {
                    script {
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