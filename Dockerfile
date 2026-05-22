# ============================================================
# Stage 1: Build con Maven
# ============================================================
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /build

# Copiar wrapper y POM primero para cachear dependencias
COPY mvnw mvnw.cmd pom.xml ./
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw dependency:go-offline -q

# Copiar fuentes y compilar (sin tests)
COPY src ./src
RUN ./mvnw package -DskipTests -q

# ============================================================
# Stage 2: Runtime JRE
# ============================================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /deployments

# Quarkus fast-jar structure
COPY --from=build /build/target/quarkus-app/lib/ lib/
COPY --from=build /build/target/quarkus-app/*.jar ./
COPY --from=build /build/target/quarkus-app/app/ app/
COPY --from=build /build/target/quarkus-app/quarkus/ quarkus/

EXPOSE 8085

ENV JAVA_OPTS="-Djava.util.logging.manager=org.jboss.logmanager.LogManager"

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar quarkus-run.jar"]
