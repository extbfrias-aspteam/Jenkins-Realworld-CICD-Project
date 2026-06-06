// Versión Final Unificada 2026
def COLOR_MAP = [
    'SUCCESS': 'good', 
    'FAILURE': 'danger',
    'UNSTABLE': 'danger'
]

pipeline {
    agent any
    
    environment {
        // Ajuste de la ruta base del Workspace por la doble carpeta del repositorio
        WORKSPACE           = "${env.WORKSPACE}/automatizacion-main/automatizacion-main"
        NEXUS_CREDENTIAL_ID = 'Nexus-Credential'
        NEXUS_USER          = "${NEXUS_CREDS_USR}"
        NEXUS_PASSWORD      = "${NEXUS_CREDS_PSWD}"
        NEXUS_URL           = "149.56.241.64:8081"
        NEXUS_REPOSITORY    = "maven_project"
        NEXUS_REPO_ID       = "maven_project"
        ARTVERSION          = "${env.BUILD_ID}"
    }
    
    tools {
        maven 'localMaven'
        jdk 'localJdk' // Apunta al Java 17 nativo de tu Docker (/opt/java/openjdk)
    }
    
    stages {
        stage('Build') {
            steps {
                dir('automatizacion-main/automatizacion-main/java-app') {
                    // Forzamos a Maven a ignorar problemas de SSL de forma segura para las descargas iniciales
                    sh 'mvn clean package -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true'
                }
            }
            post {
                success {
                    echo ' now Archiving '
                    archiveArtifacts artifacts: '**/*.war'
                }
            }
        }
        
        stage('Unit Test'){
            steps {
                dir('automatizacion-main/automatizacion-main') {
                    sh 'mvn test'
                }
            }
        }
        
        stage('Integration Test'){
            steps {
                dir('automatizacion-main/automatizacion-main') {
                    sh 'mvn verify -DskipUnitTests'
                }
            }
        }
        
        stage('Checkstyle Code Analysis'){
            steps {
                dir('automatizacion-main/automatizacion-main') {
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
                dir('automatizacion-main/automatizacion-main') {
                    withSonarQubeEnv('SonarQube') { 
                        withCredentials([string(credentialsId: 'SonarQube-Token', variable: 'SONAR_TOKEN')]) {
                            // Usamos el alias interno "sonarqube" de Docker para evitar bloqueos y timeouts de 30s
                            sh """
                            mvn clean verify sonar:sonar \
                            -Dsonar.projectKey=JavaWebApp-Project \
                            -Dsonar.host.url=http://sonarqube:9000 \
                            -Dsonar.login=${SONAR_TOKEN}
                            """
                        }
                    }
                }
            }
        }
        
        stage('SonarQube Quality Gate') {
            steps {
                timeout(time: 1, unit: 'HOURS') {
                    waitForQualityGate(abortPipeline: true)
                }
            }
        }
        
        stage("Nexus Artifact Uploader"){
            steps{
                nexusArtifactUploader(
                    nexusVersion: 'nexus3',
                    protocol: 'http',
                    nexusUrl: '149.56.241.64:8081',
                    groupId: 'webapp',
                    version: "${env.BUILD_ID}",
                    repository: 'maven-releases',
                    credentialsId: "${NEXUS_CREDENTIAL_ID}",
                    artifacts: [
                        [artifactId: 'webapp',
                        classifier: '',
                        file: "${WORKSPACE}/webapp/target/webapp.war", // Apunta al archivo .war real generado
                        type: 'war']
                    ]
                )
            }
        }
        
        stage('Deploy to Development Env') {
            environment {
                HOSTS = 'dev'
            }
            steps {
                withCredentials([usernamePassword(credentialsId: 'Ansible-Credential', passwordVariable: 'PASSWORD', usernameVariable: 'USER_NAME')]) {
                    sh "ansible-playbook -i ${WORKSPACE}/ansible-config/aws_ec2.yaml ${WORKSPACE}/deploy.yaml --extra-vars \"ansible_user=$USER_NAME ansible_password=$PASSWORD hosts=tag_Environment_$HOSTS workspace_path=${WORKSPACE}\""
                }
            }
        }
        
        stage('Deploy to Staging Env') {
            environment {
                HOSTS = 'stage'
            }
            steps {
                withCredentials([usernamePassword(credentialsId: 'Ansible-Credential', passwordVariable: 'PASSWORD', usernameVariable: 'USER_NAME')]) {
                    sh "ansible-playbook -i ${WORKSPACE}/ansible-config/aws_ec2.yaml ${WORKSPACE}/deploy.yaml --extra-vars \"ansible_user=$USER_NAME ansible_password=$PASSWORD hosts=tag_Environment_$HOSTS workspace_path=$WORKSPACE\""
                }
            }
        }
        
        stage('Quality Assurance Approval') {
            steps {
                input('Do you want to proceed to Production?')
            }
        }
        
        stage('Deploy to Production Env') {
            environment {
                HOSTS = 'prod'
            }
            steps {
                withCredentials([usernamePassword(credentialsId: 'Ansible-Credential', passwordVariable: 'PASSWORD', usernameVariable: 'USER_NAME')]) {
                    sh "ansible-playbook -i ${WORKSPACE}/ansible-config/aws_ec2.yaml ${WORKSPACE}/deploy.yaml --extra-vars \"ansible_user=$USER_NAME ansible_password=$PASSWORD hosts=tag_Environment_$HOSTS workspace_path=$WORKSPACE\""
                }
            }
        }
    }
    
    post {
        always {
            echo 'Slack Notifications.'
            slackSend channel: '#prestigious-channel',
            color: COLOR_MAP[currentBuild.currentResult],
            message: "*${currentBuild.currentResult}:* Job Name '${env.JOB_NAME}' build ${env.BUILD_NUMBER} \n Project Workspace: ${env.WORKSPACE} \n More info at: ${env.BUILD_URL}"
        }
    }
}