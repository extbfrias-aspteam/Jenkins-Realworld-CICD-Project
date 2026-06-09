def COLOR_MAP = [
    'SUCCESS': 'good', 
    'FAILURE': 'danger',
    'UNSTABLE': 'danger'
]

pipeline {
  agent any
  
  environment {
    WORKSPACE = "${env.WORKSPACE}"
    NEXUS_CREDENTIAL_ID = 'Nexus-Credential'
    NEXUS_USER = "$NEXUS_CREDS_USR"
    NEXUS_PASSWORD = "$NEXUS_CREDS_PSWD"
    NEXUS_URL = "nexus:8081"
    NEXUS_REPOSITORY = "maven-project-releases"
    NEXUS_REPO_ID    = "maven_project"
    ARTVERSION = "${env.BUILD_ID}"
  }
  
  tools {
    maven 'localMaven'
    jdk 'localJdk8'
  }
  
  stages {
    stage('Debug Workspace') {
        steps {
            echo '=== Verificando qué archivos descargó Git realmente ==='
            // Este comando listará las carpetas en tu log de Jenkins para saber dónde quedó el pom.xml
            sh 'ls -la'
        }
    }

    stage('Build') {
      steps {
        echo '=== Iniciando Compilación apuntando al subdirectorio serviciosstd_ws ==='
        sh 'mvn clean package -f serviciosstd_ws/pom.xml -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true'
      }
      post {
        success {
          echo '=== Archivando el Artefacto .war Generado ==='
          archiveArtifacts artifacts: 'serviciosstd_ws/target/ServiciosSTD_WS.war'
        }
      }
    }
    
    stage('Unit Test'){
        steps {
          sh 'mvn test -f serviciosstd_ws/pom.xml'
        }
    }
    
    stage('Integration Test'){
        steps {
          sh 'mvn verify -f serviciosstd_ws/pom.xml -DskipUnitTests'
        }
    }
    
    stage('Checkstyle Code Analysis'){
        steps {
          sh 'mvn checkstyle:checkstyle -f serviciosstd_ws/pom.xml'
        }
    }
    
    stage('SonarQube Inspection') {
        steps {
            withSonarQubeEnv('SonarQube') { 
                withCredentials([string(credentialsId: 'SonarQube-Token', variable: 'SONAR_TOKEN')]) {
                    sh """
                    mvn clean verify sonar:sonar -f serviciosstd_ws/pom.xml \
                    -Dsonar.projectKey=JavaWebApp-Project \
                    -Dsonar.host.url=http://sonarqube:9000 \
                    -Dsonar.login=$SONAR_TOKEN
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
              version: "${BUILD_ID}",
              repository: "${NEXUS_REPOSITORY}",
              credentialsId: "${NEXUS_CREDENTIAL_ID}",
              artifacts: [
                  [artifactId: 'ServiciosSTD_WS',
                  classifier: '',
                  file: "serviciosstd_ws/target/ServiciosSTD_WS.war",
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
  }
  
  post {
    always {
        slackSend channel: 'apc-cicd-2026', 
        color: COLOR_MAP[currentBuild.currentResult],
        message: "*${currentBuild.currentResult}:* Proyecto '${env.JOB_NAME}' - Build #${env.BUILD_NUMBER} \n Más información en: ${env.BUILD_URL}"
    }
  }
}