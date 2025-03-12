# Use the official Maven image to build the app
# https://hub.docker.com/_/maven
FROM maven:3.8.4-openjdk-17 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

# Use the official OpenJDK image to run the app
# https://hub.docker.com/_/openjdk
FROM openjdk:17-jdk-slim
COPY --from=build /home/app/target/resumeBuilder-0.0.1-SNAPSHOT.jar /usr/local/lib/resumeBuilder.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/resumeBuilder.jar"]