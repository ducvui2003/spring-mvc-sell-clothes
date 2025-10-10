# Use an official OpenJDK 21 image
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built WAR file into the container
COPY target/web-sell-spring-mvc-1.0.war /app/app.war

ENV SPRING_DATASOURCE_PASSWORD="root"
ENV SPRING_DATASOURCE_URL="jdbc:mysql://"
ENV SPRING_DATASOURCE_USERNAME="root"

# Expose port 8080 for the Spring Boot app
EXPOSE 8080

# Run the application with embedded Tomcat
CMD ["java", "-jar", "/app/app.war"]

# build external: /mvnw clean package -DskipTests                                                      ✔  took 11s   at 23:03:50  

## docker build -t spring-mvc-app .
## docker run -p 8080:8080 \
#   --name spring-mvc-app \
#   -e SPRING_DATASOURCE_URL=jdbc:mysql://127.0.0.1:3306/your_style \
#   -e SPRING_DATASOURCE_USERNAME=root \
#   -e SPRING_DATASOURCE_PASSWORD=securepassword \
#   spring-mvc-app