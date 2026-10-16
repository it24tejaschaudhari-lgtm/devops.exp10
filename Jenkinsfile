
pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'tejas8401/taskbox-backend'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Maven Build and Selenium Test') {
            steps {
                dir('taskbox-backend') {
                    bat 'mvn -B clean verify'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                dir('taskbox-backend') {
                    bat 'docker build -t %DOCKER_IMAGE%:%BUILD_NUMBER% .'
                    bat 'docker tag %DOCKER_IMAGE%:%BUILD_NUMBER% %DOCKER_IMAGE%:latest'
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-credentials',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    powershell '''
                        $ErrorActionPreference = "Stop"
                        $env:DOCKER_PASS | docker login --username $env:DOCKER_USER --password-stdin
                        if ($LASTEXITCODE -ne 0) {
                            exit $LASTEXITCODE
                        }
                    '''

                    bat 'docker push %DOCKER_IMAGE%:%BUILD_NUMBER%'
                    bat 'docker push %DOCKER_IMAGE%:latest'
                    bat 'docker logout'
                }
            }
        }
    }

    post {
        always {
            dir('taskbox-backend') {
                junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
                archiveArtifacts allowEmptyArchive: true, artifacts: 'target/*.jar'
            }
        }

        success {
            echo 'TaskBox DevOps pipeline completed successfully.'
        }

        failure {
            echo 'Pipeline failed. Inspect the failed stage and Console Output.'
        }
    }
}
