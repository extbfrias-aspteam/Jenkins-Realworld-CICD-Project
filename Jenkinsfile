// DEFINICIÓN DE COLORES PARA LAS NOTIFICACIONES DE SLACK
def COLOR_MAP = [
    'SUCCESS': 'good', 
    'FAILURE': 'danger',
    'UNSTABLE': 'danger'
]

pipeline {
  agent any
  
  environment {
    // El espacio de trabajo raíz donde se descarga tu código de GitHub
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
    // Llama a las herramientas fijas que configuramos en la sección "Tools" de Jenkins
    maven 'localMaven'
    jdk 'localJdk8'
  }
  
  stages {
    stage('Clean Workspace') {
        steps {
            echo '=== Limpiando residuos del espacio de trabajo antes de iniciar ==='
            cleanWs()
        }
    }

    stage('Build') {
      steps {
        echo '=== Iniciando Compilación apuntando al subdirectorio serviciosstd_ws ==='
        // Forzamos a Maven a leer el pom.xml correcto usando el flag -f
        sh 'mvn clean package -f serviciosstd_ws/pom.xml -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true'
      }
      post {
        success {
          echo '=== Archivando el Artefacto .war Generado ==='
          // Modificado para guardar el binario desde la subcarpeta target correcta
          archiveArtifacts artifacts: 'serviciosstd_ws/target/ServiciosSTD_WS.war'
        }
      }
    }
    
    stage('Unit Test'){
        steps {
          echo '=== Ejecutando Pruebas Unitarias ==='
          sh 'mvn test -f serviciosstd_ws/pom.xml'
        }
    }
    
    stage('Integration Test'){
        steps {
          echo '=== Ejecutando Pruebas de Integración ==='
          sh 'mvn verify -f serviciosstd_ws/pom.xml -DskipUnitTests'
        }
    }
    
    stage('Checkstyle Code Analysis'){
        steps {
          echo '=== Analizando Calidad de Código con Checkstyle ==='
          sh 'mvn checkstyle:checkstyle -f serviciosstd_ws/pom.xml'
        }
    }
    
    stage('SonarQube Inspection') {
        steps {
            echo '=== Lanzando Análisis hacia el Servidor SonarQube ==='
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
            echo '=== Esperando Aprobación del Quality Gate ==='
            timeout(time: 1, unit: 'HOURS') {
                waitForQualityGate(abortPipeline: true)
            }
        }
    }
    
    stage("Nexus Artifact Uploader"){
        steps{
            echo '=== Subiendo el archivo WAR a Sonatype Nexus ==='
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
                  file: "serviciosstd_ws/target/ServiciosSTD_WS.war", // Ruta corregida apuntando a la subcarpeta
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
            echo '=== Desplegando en Entorno de Desarrollo (Ansible) ==='
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
            echo '=== Desplegando en Entorno de Staging (Ansible) ==='
            withCredentials([usernamePassword(credentialsId: 'Ansible-Credential', passwordVariable: 'PASSWORD', usernameVariable: 'USER_NAME')]) {
                sh "ansible-playbook -i ${WORKSPACE}/ansible-config/aws_ec2.yaml ${WORKSPACE}/deploy.yaml --extra-vars \"ansible_user=$USER_NAME ansible_password=$PASSWORD hosts=tag_Environment_$HOSTS workspace_path=$WORKSPACE\""
            }
        }
    }
    
    stage('Quality Assurance Approval') {
        steps {
            input('¿Deseas proceder con el despliegue al entorno de Producción?')
        }
    }
    
    stage('Deploy to Production Env') {
        environment {
            HOSTS = 'prod'
        }
        steps {
            echo '=== ¡DESPLEGANDO EN PRODUCCIÓN! ==='
            withCredentials([usernamePassword(credentialsId: 'Ansible-Credential', passwordVariable: 'PASSWORD', usernameVariable: 'USER_NAME')]) {
                sh "ansible-playbook -i ${WORKSPACE}/ansible-config/aws_ec2.yaml ${WORKSPACE}/deploy.yaml --extra-vars \"ansible_user=$USER_NAME ansible_password=$PASSWORD hosts=tag_Environment_$HOSTS workspace_path=$WORKSPACE\""
            }
        }
    }
  }
  
  post {
    always {
        echo '=== Enviando Notificación de Estado a Slack ==='
        slackSend channel: 'apc-cicd-2026', 
        color: COLOR_MAP[currentBuild.currentResult],
        message: "*${currentBuild.currentResult}:* Proyecto '${env.JOB_NAME}' - Build #${env.BUILD_NUMBER} \n Timestamp: ${env.BUILD_TIMESTAMP} \n Más información en: ${env.BUILD_URL}"
    }
  }
}