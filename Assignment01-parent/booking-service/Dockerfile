#-----------------
# BUILD STAGE
#-----------------

FROM gradle:8-jdk22-alpine AS builder

# Set the working directory - the location of the source code
COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src

RUN gradle build -x test

#-----------------
# BUILD STAGE
#-----------------
FROM openjdk:22-jdk

RUN mkdir /app

# CPOY --from=builder means that the files from the builder stage are copied to the current stage
COPY --from=builder /home/gradle/src/build/libs/*.jar /app/booking-service.jar

# ENV means that the environment variables are set in the container

ENV MONGO_DB_USERNAME=admin \
    MONGO_DB_PWD=password


EXPOSE 8095

#ENTYPOINT means that the container will run the command that is specified
# in the ENTRYPOINT when the container starts.
ENTRYPOINT ["java", "-jar", "/app/booking-service.jar"]