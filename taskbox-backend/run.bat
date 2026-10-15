@echo off
echo Starting TaskBox Backend with MINIMAL memory settings...
echo.
echo WARNING: Using very low memory settings (128MB heap)
echo Close other applications to free up RAM!
echo.

REM Set JVM memory options - MINIMAL SETTINGS
set MAVEN_OPTS=-Xms128m -Xmx256m -XX:MaxMetaspaceSize=128m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -Xss256k

REM Run Spring Boot application with minimal memory
call mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xms128m -Xmx256m -XX:MaxMetaspaceSize=128m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -Xss256k"

pause

