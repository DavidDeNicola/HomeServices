FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package

FROM tomcat:11.0-jdk17
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/HomeServices.war
EXPOSE 8080