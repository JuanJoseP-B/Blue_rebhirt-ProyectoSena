FROM maven:3.9.9-eclipse-temurin-17 AS compilacion
WORKDIR /proyecto
COPY backend/pom.xml ./pom.xml
RUN mvn -B dependency:go-offline
COPY backend/src ./src
RUN mvn -B package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /aplicacion
COPY --from=compilacion /proyecto/target/bluerebhirt-0.0.1-SNAPSHOT.jar aplicacion.jar
EXPOSE 8080
CMD ["java", "-jar", "aplicacion.jar"]
