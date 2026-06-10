def COLOR_MAP = [
    'SUCCESS': 'good', 
    'FAILURE': 'danger',
    'UNSTABLE': 'danger'
]

pipeline {
    agent any
    
    // ── NUEVO DISPARADOR AUTOMÁTICO DE WEBHOOK ──
    triggers {
        githubPush() // Esto le dice a Jenkins: "Reacciona de inmediato cuando GitHub te avise"
    }

    environment {
        WORKSPACE = "${env.WORKSPACE}/serviciosstd_ws"
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
        jdk 'localJdk'
    }
    
    stages {
        stage('Build') {
            steps {
                dir('serviciosstd_ws/') {
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
                dir('serviciosstd_ws/') {
                    sh 'mvn test'
                } 
            }
        }

        stage('Integration Test'){
            steps {
                dir('serviciosstd_ws/') {
                    sh 'mvn verify -DskipUnitTests'
                }
            }
        }

        stage ('Checkstyle Code Analysis'){
            steps {
                dir('serviciosstd_ws/') {
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
                withSonarQubeEnv('SonarQube') { 
                    withCredentials([string(credentialsId: 'SonarQube-Token', variable: 'SONAR_TOKEN')]) {
                        // CORREGIDO: Se agrega \$ para que Linux maneje la credencial de forma segura
                        sh """
                        mvn clean verify sonar:sonar \
                        -Dsonar.projectKey=JavaWebApp-Project \
                        -Dsonar.host.url=http://sonarqube:9000 \
                        -Dsonar.login=\$SONAR_TOKEN
                        """
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
                    nexusUrl: 'nexus:8081',
                    groupId: 'webapp',
                    version: "${env.BUILD_ID}-${env.BUILD_TIMESTAMP}",
                    repository: 'maven-releases',
                    credentialsId: "${NEXUS_CREDENTIAL_ID}",
                    artifacts: [
                        [artifactId: 'webapp',
                        classifier: '',
                        file: "${WORKSPACE}/target/ServiciosSTD_WS.war", // Apuntamos directo al WAR real generado
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
                    sh "ansible-playbook -i ${env.WORKSPACE}/ansible-config/aws_ec2.yaml ${env.WORKSPACE}/deploy.yaml --extra-vars \"ansible_user=$USER_NAME ansible_password=$PASSWORD hosts=tag_Environment_$HOSTS workspace_path=${env.WORKSPACE}\""
                }
            }
        }

        stage('Deploy to Staging Env') {
            environment {
                HOSTS = 'stage'
            }
            steps {
                withCredentials([usernamePassword(credentialsId: 'Ansible-Credential', passwordVariable: 'PASSWORD', usernameVariable: 'USER_NAME')]) {
                    sh "ansible-playbook -i ${env.WORKSPACE}/ansible-config/aws_ec2.yaml ${env.WORKSPACE}/deploy.yaml --extra-vars \"ansible_user=$USER_NAME ansible_password=$PASSWORD hosts=tag_Environment_$HOSTS workspace_path=${env.WORKSPACE}\""
                }
            }
        }

        stage('Quality Assurance Approval') {
            steps {
                input('Do you want to proceed?')
            }
        }

        stage('Deploy to Production Env') {
            environment {
                HOSTS = 'prod'
            }
            steps {
                withCredentials([usernamePassword(credentialsId: 'Ansible-Credential', passwordVariable: 'PASSWORD', usernameVariable: 'USER_NAME')]) {
                    sh "ansible-playbook -i ${env.WORKSPACE}/ansible-config/aws_ec2.yaml ${env.WORKSPACE}/deploy.yaml --extra-vars \"ansible_user=$USER_NAME ansible_password=$PASSWORD hosts=tag_Environment_$HOSTS workspace_path=${env.WORKSPACE}\""
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