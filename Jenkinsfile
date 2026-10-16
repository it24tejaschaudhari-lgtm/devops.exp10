
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
                    credentialsId: 'dockerhub-token-new',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    powershell '''
                        $ErrorActionPreference = "Stop"

                        $startInfo = New-Object System.Diagnostics.ProcessStartInfo
                        $startInfo.FileName = "docker.exe"
                        $startInfo.Arguments = "login --username $env:DOCKER_USER --password-stdin"
                        $startInfo.UseShellExecute = $false
                        $startInfo.RedirectStandardInput = $true
                        $startInfo.RedirectStandardOutput = $true
                        $startInfo.RedirectStandardError = $true
                        $startInfo.CreateNoWindow = $true

                        $process = New-Object System.Diagnostics.Process
                        $process.StartInfo = $startInfo
                        [void]$process.Start()

                        $process.StandardInput.WriteLine($env:DOCKER_PASS.Trim())
                        $process.StandardInput.Close()

                        $stdout = $process.StandardOutput.ReadToEnd()
                        $stderr = $process.StandardError.ReadToEnd()
                        $process.WaitForExit()

                        if ($stdout) { Write-Output $stdout }
                        if ($process.ExitCode -ne 0) {
                            Write-Error "Docker login failed. Check credentials or network."
                            exit 1
                        }
                    '''

                    bat 'docker push %DOCKER_IMAGE%:%BUILD_NUMBER%'
                    bat 'docker push %DOCKER_IMAGE%:latest'
                    bat 'docker logout'
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
}