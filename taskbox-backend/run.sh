#!/bin/bash
echo "Starting TaskBox Backend with reduced memory settings..."
echo ""

# Set JVM memory options (reduce these if still getting errors)
export MAVEN_OPTS="-Xms256m -Xmx512m -XX:MaxMetaspaceSize=256m"

# Run Spring Boot application
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xms256m -Xmx512m -XX:MaxMetaspaceSize=256m"

