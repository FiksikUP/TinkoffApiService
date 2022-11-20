FROM maven:3.8.5-openjdk-17 as build

COPY src src
COPY pom.xml .
RUN mvn clean package -Dmaven.test.skip=true

FROM openjdk:17-jdk as production

WORKDIR /application

COPY src src
COPY --from=build /target/TinkoffApiService-1.0.0-SNAPSHOT.war TinkoffApiService-1.0.0-SNAPSHOT.war
RUN chmod -R 777 /application
EXPOSE 8092

ENTRYPOINT ["java", "-Xms128m", "-Xss16m" , "-Xmx1024m", "-jar", "TinkoffApiService-1.0.0-SNAPSHOT.war"]