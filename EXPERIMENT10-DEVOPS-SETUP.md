# TaskBox — Experiment 10 DevOps Setup

## Project facts
- Frontend: HTML, CSS, vanilla JavaScript (`frontend/`)
- Backend: Java 17 / Spring Boot / Maven (`taskbox-backend/`)
- Database: MySQL
- Selenium UI smoke test: `taskbox-backend/src/test/java/com/taskbox/TaskboxFrontendSeleniumTest.java`

## Before you push these changes
1. Review `taskbox-backend/src/main/resources/application.properties`. It previously contained a database password in the repository. Rotate that password immediately, remove credentials from tracked files/history where possible, and use environment variables or an untracked local configuration file. The updated `application.properties` now reads the database password from `DB_PASSWORD`; set this environment variable before running the backend locally. Do not publish secrets.
2. Create a Docker Hub repository, for example `taskbox-backend`.
3. Replace `YOUR_DOCKERHUB_USERNAME` in the root `Jenkinsfile`.
4. Ensure the Jenkins Windows agent has Git, JDK 17 or later, Maven, Docker Desktop, and Google Chrome available. The Selenium test uses Chrome headless.
5. Add a Jenkins credential of type “Username with password”, ID `dockerhub-credentials`, with your Docker Hub username and an access token as the password.

## Run locally (PowerShell)
From repository root:
```powershell
cd taskbox-backend
mvn.cmd -B clean verify
```
The Selenium smoke test opens `../frontend/index.html`, so run Maven from the `taskbox-backend` directory. Chrome must be installed; Selenium Manager may need internet access on first run.

## Push to GitHub
From repository root:
```powershell
git add Jenkinsfile EXPERIMENT10-DEVOPS-SETUP.md taskbox-backend/pom.xml taskbox-backend/Dockerfile taskbox-backend/.dockerignore taskbox-backend/src/main/resources/application-docker.properties taskbox-backend/src/test/java/com/taskbox/TaskboxFrontendSeleniumTest.java
git commit -m "Add Experiment 10 DevOps pipeline"
git push
```
Do not use `git add .` until you have checked that no secrets, uploaded assignments, or generated files are staged.

## Jenkins
Create a Pipeline job → Pipeline script from SCM → Git → repository URL → branch `*/main` (or your actual default branch) → Script Path `Jenkinsfile`.

Add credentials:
- Manage Jenkins → Credentials → (global) → Add Credentials
- Kind: Username with password
- ID: `dockerhub-credentials`
- Username: Docker Hub username
- Password: Docker Hub access token

Click Build Now. Stages: Checkout → Maven Build and Selenium Test → Build Docker Image → Push to Docker Hub.

## Docker image
The Dockerfile builds the backend image. The running backend needs a reachable MySQL server and the `SPRING_DATASOURCE_PASSWORD` and `JWT_SECRET` environment variables. The provided Docker profile assumes a MySQL service reachable as `mysql:3306`; for a standalone container, configure a reachable database hostname and environment variables. Image build/push does not itself prove the app is deployed or database-connected.

## Screenshots
1. Existing TaskBox frontend open in browser.
2. GitHub repository showing root `Jenkinsfile`, backend `pom.xml`, `Dockerfile`, and Selenium test.
3. Maven output showing `BUILD SUCCESS` and Selenium test pass.
4. Jenkins pipeline stage view.
5. Jenkins console output / JUnit test result.
6. Docker image tag in `docker images` or Docker Desktop.
7. Docker Hub repository showing pushed build tag and `latest`.

Capture only screenshots of results that actually occurred.
