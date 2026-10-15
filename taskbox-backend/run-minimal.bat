@echo off
echo ========================================
echo TaskBox Backend - ULTRA MINIMAL MODE
echo ========================================
echo.
echo This uses the ABSOLUTE minimum memory.
echo Close ALL other programs before running!
echo.
pause

REM Ultra minimal JVM settings
set MAVEN_OPTS=-Xms64m -Xmx128m -XX:MaxMetaspaceSize=96m -XX:+UseSerialGC -Xss128k

REM Skip tests and use offline mode to save memory
call mvn clean package -DskipTests -o
if errorlevel 1 (
    echo Build failed! Trying with internet...
    call mvn clean package -DskipTests
)

if errorlevel 1 (
    echo Build failed completely. Check errors above.
    pause
    exit /b 1
)

echo.
echo Starting application with minimal memory...
java -Xms64m -Xmx128m -XX:MaxMetaspaceSize=96m -XX:+UseSerialGC -Xss128k -jar target\taskbox-backend-1.0.0.jar

pause


