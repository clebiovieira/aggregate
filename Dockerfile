# Image for build the project
FROM maven:3.6.3-jdk-8-slim as maven

# copy pom to get off-line dependencies
COPY ./pom.xml ./pom.xml

# copy your other files
COPY ./src ./src

# build up project
RUN mvn dependency:go-offline package -B

# Create a new light container just with compiled jar
FROM openjdk:8-jdk-alpine

COPY prefixes.txt prefixes.txt

COPY --from=maven target/aggregate-*.jar ./app.jar

ENTRYPOINT ["java","-jar","app.jar"]