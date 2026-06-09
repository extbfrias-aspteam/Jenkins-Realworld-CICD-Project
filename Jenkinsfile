// TEST
def COLOR_MAP = [
    'SUCCESS': 'good', 
    'FAILURE': 'danger',
    'UNSTABLE': 'danger'
]
pipeline {
  agent any
  environment {
    //WORKSPACE = "${env.WORKSPACE}"
    WORKSPACE = "${env.WORKSPACE}/serviciosstd_ws"
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
    jdk 'localJdk'
  }
  
  stages {
    stage('Build') {
      steps {
        dir('serviciosstd_ws/') {
    // Forzamos a Maven a ignorar la validación estricta de SSL para este paso
            sh 'mvn clean package -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true'           //sh 'mvn clean package -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true'
        }
        //dir('automatizacion-main/automatizacion-main/java-app') {
          //  sh 'mvn org.apache.maven.plugins:maven-resources-plugin:3.3.1:resources clean package -Dhttps.protocols=TLSv1.2,TLSv1.3'  //sh 'mvn clean package'
            //}
        //dir('realworld-cicd-pipeline-project-main/') {
        
        //sh 'mvn clean package'
       // }
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
         //dir('realworld-cicd-pipeline-project-main/') {
         sh 'mvn test'
        // }
        }
    }
    stage('Integration Test'){
        steps {
         //dir('realworld-cicd-pipeline-project-main/') {
          sh 'mvn verify -DskipUnitTests'
        //}
        }
    }
    stage ('Checkstyle Code Analysis'){
        steps {
           // dir('realworld-cicd-pipeline-project-main/') {
            sh 'mvn checkstyle:checkstyle'
        //}
        }
        post {
            success {
                echo 'Generated Analysis Result'
            }
        }
    }
    stage('SonarQube Inspection') {
        steps {
            dir('serviciosstd_ws/') {
            withSonarQubeEnv('SonarQube') { 
                withCredentials([string(credentialsId: 'SonarQube-Token', variable: 'SONAR_TOKEN')]) {
                    
                    sh """
                    mvn clean verify sonar:sonar \
                    -Dsonar.projectKey=JavaWebApp-Project \
                    -Dsonar.host.url=http://sonarqube:9000 \
                    -Dsonar.login=$SONAR_TOKEN
                    """
                }
            }
            }
        }
    }

           // dir('realworld-cicd-pipeline-project-main/') {
            //withSonarQubeEnv('SonarQube') { 
            //    withCredentials([string(credentialsId: 'SonarQube-Token', variable: 'SONAR_TOKEN')]) {
            //    sh """
            //    mvn clean verify sonar:sonar \
            //    -Dsonar.projectKey=JavaWebApp-Project \
            //    -Dsonar.host.url=http://149.56.241.64:9000 \
            //    -Dsonar.login=$SONAR_TOKEN
            //    """
            //    }
            //}
           // }
        //}
    //}
    stage('SonarQube Quality Gate') {
        steps {
          // Set a timeout for the quality gate check
            timeout(time: 1, unit: 'HOURS') {
            // Wait for the SonarQube quality gate result and abort the pipeline if it fails
            waitForQualityGate(abortPipeline: true)
        }
    }

    }
    stage("Nexus Artifact Uploader"){
        steps{
          // dir('realworld-cicd-pipeline-project-main/') {
           nexusArtifactUploader(
              nexusVersion: 'nexus3',
              protocol: 'http',
              nexusUrl: 'nexus:8081',
              groupId: 'webapp',
              version: "${BUILD_ID}",
              repository: "${NEXUS_REPOSITORY}",
              credentialsId: "${NEXUS_CREDENTIAL_ID}",
              artifacts: [
                  [artifactId: 'webapp',
                  classifier: '',
                  file: "webapp/target/webapp.war",
                  type: 'war']
              ]
           )
        //}
        }
    }
    stage('Deploy to Development Env') {
        environment {
            HOSTS = 'dev'
        }
        steps {
            //dir('realworld-cicd-pipeline-project-main/') {
            withCredentials([usernamePassword(credentialsId: 'Ansible-Credential', passwordVariable: 'PASSWORD', usernameVariable: 'USER_NAME')]) {
                sh "ansible-playbook -i ${WORKSPACE}/ansible-config/aws_ec2.yaml ${WORKSPACE}/deploy.yaml --extra-vars \"ansible_user=$USER_NAME ansible_password=$PASSWORD hosts=tag_Environment_$HOSTS workspace_path=${WORKSPACE}\""
            }
          //}
        }

    }
    stage('Deploy to Staging Env') {
        environment {
            HOSTS = 'stage'
        }
        steps {
           // dir('realworld-cicd-pipeline-project-main/') {
            withCredentials([usernamePassword(credentialsId: 'Ansible-Credential', passwordVariable: 'PASSWORD', usernameVariable: 'USER_NAME')]) {
                sh "ansible-playbook -i ${WORKSPACE}/ansible-config/aws_ec2.yaml ${WORKSPACE}/deploy.yaml --extra-vars \"ansible_user=$USER_NAME ansible_password=$PASSWORD hosts=tag_Environment_$HOSTS workspace_path=$WORKSPACE\""
            }
            //}
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
           //dir('realworld-cicd-pipeline-project-main/') {
            withCredentials([usernamePassword(credentialsId: 'Ansible-Credential', passwordVariable: 'PASSWORD', usernameVariable: 'USER_NAME')]) {
                sh "ansible-playbook -i ${WORKSPACE}/ansible-config/aws_ec2.yaml ${WORKSPACE}/deploy.yaml --extra-vars \"ansible_user=$USER_NAME ansible_password=$PASSWORD hosts=tag_Environment_$HOSTS workspace_path=$WORKSPACE\""
            }
          // }
          //comment
        }
         }
  }
  post {
    always {
        echo 'Slack Notifications.'
        slackSend channel: 'apc-cicd-2026', //update and provide your channel name
        color: COLOR_MAP[currentBuild.currentResult],
        message: "*${currentBuild.currentResult}:* Job Name '${env.JOB_NAME}' build ${env.BUILD_NUMBER} \n Build Timestamp: ${env.BUILD_TIMESTAMP} \n Project Workspace: ${env.WORKSPACE} \n More info at: ${env.BUILD_URL}"
    }
  }

  
}

