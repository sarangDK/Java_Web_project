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

COPY --from=builder /home/gradle/src/build/libs/*.jar /app/room-service.jar

ENV PGADMIN_DEFAULT_EMAIL=user@domain.ca \
    PGADMIN_DEFAULT_PASSWORD=password



EXPOSE 8091

#ENTYPOINT means that the container will run the command that is specified
# in the ENTRYPOINT when the container starts.
ENTRYPOINT ["java", "-jar", "/app/room-service.jar"]