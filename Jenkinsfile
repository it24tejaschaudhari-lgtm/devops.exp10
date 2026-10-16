
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
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-credentials',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )
                ]) 
                      
                    powershell '''
                        $ErrorActionPreference = "Stop"

                        $psi = New-Object System.Diagnostics.ProcessStartInfo
                        $psi.FileName = "docker.exe"
                        $psi.Arguments = "login --username $env:DOCKER_USER --password-stdin"
                        $psi.UseShellExecute = $false
                        $psi.RedirectStandardInput = $true
                        $psi.RedirectStandardOutput = $true
                        $psi.RedirectStandardError = $true
                        $psi.CreateNoWindow = $true

                        $p = New-Object System.Diagnostics.Process
                        $p.StartInfo = $psi
                        [void]$p.Start()

                        $p.StandardInput.WriteLine($env:DOCKER_PASS)
                        $p.StandardInput.Close()

                        $outTask = $p.StandardOutput.ReadToEndAsync()
                        $errTask = $p.StandardError.ReadToEndAsync()
                        $p.WaitForExit()

                        $out = $outTask.Result
                        $err = $errTask.Result

                        if ($out) { Write-Output $out }

                        if ($p.ExitCode -ne 0) {
                            Write-Error "Docker login failed. Verify the Jenkins credential."
                            exit 1
                        }
                    '''

                }
            }
        }
    }

    post {
        always {
            dir('taskbox-backend') {
                junit allowEmptyResults: true,
                    testResults: 'target/surefire-reports/*.xml'

                archiveArtifacts allowEmptyArchive: true,
                    artifacts: 'target/*.jar'
            }
        }

        success {
            echo 'TaskBox DevOps pipeline completed successfully.'
        }

        failure {
            echo 'Pipeline failed. Inspect the failed stage and Console Output.'
        }
    }

