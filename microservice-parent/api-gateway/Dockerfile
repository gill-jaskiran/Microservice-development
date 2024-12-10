# --------------
# BUID STAGE
#---------------

FROM gradle:8-jdk22-alpine AS builder

COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src

RUN gradle build -x test

# ---------------
# PACKAGE STAGE
#----------------
FROM openjdk:22-jdk

RUN mkdir /app

COPY --from=builder /home/gradle/src/build/libs/*.jar /app/api-gateway.jar

ENV MONGO_DB_USERNAME=admin \
    MONGO_DB_PWD=password

EXPOSE 9000

ENTRYPOINT ["java", "-jar", "/app/api-gateway.jar.jar"]

# when refactoring change the expose port number and the name of the . jar file