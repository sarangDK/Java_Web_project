# ----------------
# Build Stage
# ----------------

FROM gradle:8-jdk22-alpine AS builder
  
  # Set the working directory - the location of the source code
COPY --chown=gradle:grade . /home/gradle/src

WORKDIR /home/gradle/src

RUN gradle build -x test
  
  
  # ----------------
  # Package Stage
  # ----------------
  
  #Using openjdk:22-jdk image to create final container
FROM openjdk:22-jdk
  
  #create /app dir inside container
RUN mkdir /app
  
  #copy already created jar file inside of /home/gradle/src/libs into /app folder
  #and name it product-service.jar
COPY --from=builder /home/gradle/src/build/libs/*.jar /app/api-gateway.jar
  

EXPOSE 9000
  
  #ENTYPOINT means that the container will run the command that is specified in the ENTRYPOINT when the container starts.
ENTRYPOINT ["java", "-jar", "/app/api-gateway.jar"]
