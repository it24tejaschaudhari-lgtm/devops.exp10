# TaskBox - Academic Assignment Management System

A full-stack web application for managing academic assignments, submissions, and student-teacher interactions.

## 📁 Project Structure

```
TaskBox/
├── frontend/              # Frontend files (HTML, CSS, JavaScript)
│   ├── index.html
│   ├── app.js
│   ├── style.css
│   └── taskbox-logo.png
├── taskbox-backend/       # Spring Boot backend API
│   ├── src/
│   ├── pom.xml
│   ├── README.md
│   └── run.bat
└── README.md             # This file
```

## 🚀 Quick Start

### Prerequisites
- **Java 17+** - [Download](https://www.oracle.com/java/technologies/downloads/)
- **Maven 3.6+** - [Download](https://maven.apache.org/download.cgi)
- **MySQL 8.0+** - [Download](https://dev.mysql.com/downloads/mysql/)
- **Python 3** (for frontend server) or any web server

### 1. Database Setup

```bash
# Start MySQL
net start MySQL80  # Windows
# or
sudo systemctl start mysql  # Linux/Mac

# Create database
mysql -u root -p
CREATE DATABASE taskbox_db;
USE taskbox_db;
SOURCE taskbox-backend/src/main/resources/database_schema.sql;
```

### 2. Backend Setup

```bash
cd taskbox-backend

# Configure database password in:
# src/main/resources/application.properties
# (Update spring.datasource.password)

# Run backend
.\run.bat  # Windows
# or
./run.sh   # Linux/Mac
# or
mvn spring-boot:run
```

**Backend runs on:** `http://localhost:8080`

### 3. Frontend Setup

```bash
# Navigate to frontend folder
cd frontend

# Start simple HTTP server
python -m http.server 8000

# Or use any web server:
# - Live Server (VS Code extension)
# - http-server (npm install -g http-server)
```

**Frontend runs on:** `http://localhost:8000`

### 4. Access Application

Open browser: `http://localhost:8000`

**Default Credentials:**
- **Student:** `aarav.singh@taskbox.edu` / `Student@123`
- **Teacher:** `rajesh.kumar@taskbox.edu` / `Teacher@123`

## 📚 Features

- ✅ User Authentication (Login/Register with OTP)
- ✅ Assignment Management
- ✅ File Upload/Download
- ✅ Submission Tracking
- ✅ Notifications
- ✅ Role-based Access (Student/Teacher)

## 🛠️ Technology Stack

### Frontend
- HTML5, CSS3, JavaScript (Vanilla)
- No frameworks required

### Backend
- Spring Boot 3.2.0
- Spring Data JPA
- Spring Security
- MySQL Database
- JWT Authentication

## 📖 Documentation

- **Backend Documentation:** See `taskbox-backend/README.md`
- **API Endpoints:** See `taskbox-backend/README.md#api-endpoints`

## 🔧 Troubleshooting

### Backend won't start
- Check MySQL is running
- Verify database credentials in `application.properties`
- Check port 8080 is available
- Run `.\diagnose.bat` in `taskbox-backend/` folder

### CORS Errors
- Ensure backend is running on `http://localhost:8080`
- Check `WebConfig.java` allows your frontend origin

### Memory Issues
- Use `run-minimal.bat` for low-memory systems
- See `taskbox-backend/TROUBLESHOOTING.md`

## 📝 Configuration

### Database
Edit `taskbox-backend/src/main/resources/application.properties`:
```properties
spring.datasource.password=YOUR_PASSWORD
```

### API URL
Edit `frontend/app.js`:
```javascript
const API_BASE_URL = 'http://localhost:8080/api';
```

## 🎯 Next Steps

1. Start MySQL database
2. Run backend: `cd taskbox-backend && .\run.bat`
3. Run frontend: `cd frontend && python -m http.server 8000`
4. Open browser: `http://localhost:8000`
5. Login and explore!

## 📄 License

This project is for educational purposes.

---

**Need Help?** Check `taskbox-backend/TROUBLESHOOTING.md` for detailed troubleshooting guide.


