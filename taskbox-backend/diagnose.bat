@echo off
echo ========================================
echo TaskBox Backend - Diagnostic Tool
echo ========================================
echo.

echo [1/5] Checking Java version...
java -version
if errorlevel 1 (
    echo ERROR: Java not found! Please install Java 17 or higher.
    pause
    exit /b 1
)
echo.

echo [2/5] Checking Maven version...
mvn -version
if errorlevel 1 (
    echo ERROR: Maven not found! Please install Maven.
    pause
    exit /b 1
)
echo.

echo [3/5] Checking if port 8080 is available...
netstat -ano | findstr :8080 >nul
if not errorlevel 1 (
    echo WARNING: Port 8080 is already in use!
    echo Finding process...
    for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080') do echo Process ID: %%a
    echo.
    echo You may need to stop this process or change the port.
) else (
    echo OK: Port 8080 is available
)
echo.

echo [4/5] Cleaning previous builds...
call mvn clean
echo.

echo [5/5] Compiling project (this may take a while)...
call mvn compile -e
if errorlevel 1 (
    echo.
    echo ========================================
    echo COMPILATION FAILED!
    echo ========================================
    echo.
    echo Please check the errors above.
    echo Common issues:
    echo - Missing dependencies: Run 'mvn clean install'
    echo - Java version mismatch: Need Java 17+
    echo - Syntax errors: Check the error messages above
    echo.
    pause
    exit /b 1
)
echo.

echo ========================================
echo COMPILATION SUCCESSFUL!
echo ========================================
echo.
echo You can now try running:
echo   mvn spring-boot:run
echo   OR
echo   .\run.bat
echo.
pause

