FROM openjdk:8-jre-alpine

COPY *.jar /app.jar

COPY test-application.yml /app.yml

ENTRYPOINT ["/usr/bin/java", "-jar", "/app.jar", "--spring.config.location=classpath:/app.yml"]