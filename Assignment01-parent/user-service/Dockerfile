#-----------------
# BUILD STAGE
#-----------------

FROM gradle:8-jdk22-alpine AS builder

# Set the working directory - the location of the source code
COPY --chown=gradle:grade . /home/gradle/src

WORKDIR /home/gradle/src

RUN gradle build -x test

#-----------------
# BUILD STAGE
#-----------------
FROM openjdk:22-jdk

RUN mkdir /app

COPY --from=builder /home/gradle/src/build/libs/*.jar /app/user-service.jar

EXPOSE 8092

ENTRYPOINT ["java", "-jar", "/app/user-service.jar"]