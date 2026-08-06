# TaskBox Backend - Spring Boot API

## Overview
Backend API for TaskBox academic assignment management system.

## Prerequisites
- Java 17+
- Maven 3.6+
- MySQL 8.0+

## Quick Start

### 1. Database Setup
```bash
# Start MySQL
net start MySQL80  # Windows
sudo systemctl start mysql  # Linux/Mac

# Create database
mysql -u root -p
CREATE DATABASE taskbox_db;
USE taskbox_db;
SOURCE src/main/resources/database_schema.sql;
```

### 2. Configure
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.password=YOUR_PASSWORD
```

### 3. Run
```bash
# Option A: Use script (recommended)
.\run.bat  # Windows
./run.sh   # Linux/Mac

# Option B: Maven
mvn spring-boot:run

# Option C: JAR
mvn clean package
java -jar target/taskbox-backend-1.0.0.jar
```

**Server runs on:** `http://localhost:8080`

## API Endpoints

### Authentication
- `POST /api/auth/login` - Login with password
- `POST /api/auth/register/student` - Register student
- `POST /api/auth/register/teacher` - Register teacher
- `POST /api/auth/otp/send` - Send OTP
- `POST /api/auth/otp/verify` - Verify OTP

### Assignments
- `GET /api/assignments` - Get all assignments
- `GET /api/assignments/{id}` - Get assignment by ID
- `POST /api/assignments` - Create assignment
- `GET /api/assignments/student/{studentId}` - Get student assignments

### Submissions
- `GET /api/submissions/assignment/{assignmentId}` - Get assignment submissions
- `GET /api/submissions/student/{studentId}` - Get student submissions
- `GET /api/submissions/{submissionId}` - Get submission by ID
- `POST /api/submissions` - Create/update submission
- `PUT /api/submissions/{submissionId}/grade` - Grade submission

### Files
- `POST /api/files/upload` - Upload submission file
- `GET /api/files/download/{submissionId}` - Download single file
- `GET /api/files/download-all/{assignmentId}` - Download all as ZIP

## Troubleshooting

### Build Errors
```bash
# Clean and rebuild
mvn clean install -DskipTests

# Check compilation
mvn compile
```

### Memory Issues
```bash
# Use minimal memory script
.\run-minimal.bat
```

### Port Already in Use
```bash
# Find process using port 8080
netstat -ano | findstr :8080

# Kill process (replace PID)
taskkill /PID <PID> /F
```

### Database Connection Failed
- Verify MySQL is running
- Check password in `application.properties`
- Ensure database `taskbox_db` exists

See `TROUBLESHOOTING.md` for more details.

## Project Structure

```
taskbox-backend/
├── src/main/java/com/taskbox/
│   ├── config/          # Security, CORS configuration
│   ├── controller/      # REST API endpoints
│   ├── dto/             # Data transfer objects
│   ├── entity/          # JPA entities (database models)
│   ├── repository/      # Data access layer
│   ├── service/         # Business logic
│   └── TaskboxBackendApplication.java
├── src/main/resources/
│   ├── application.properties  # Configuration
│   └── database_schema.sql      # Database schema
├── pom.xml              # Maven dependencies
└── run.bat             # Run script (Windows)
```

## Configuration

### Database
`src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/taskbox_db
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### File Upload
Files are stored in: `uploads/` directory (created automatically)

### Memory Settings
If you have low RAM, use `run-minimal.bat` or set:
```bash
set MAVEN_OPTS=-Xms128m -Xmx256m
```

## Development

### Hot Reload
Spring Boot DevTools included - code changes auto-reload

### Testing API
Use Postman, curl, or browser:
```bash
# Test endpoint
curl http://localhost:8080/api/assignments
```

### View Database
Use MySQL Workbench to inspect tables and data.
