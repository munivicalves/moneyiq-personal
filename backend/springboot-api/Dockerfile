# ── Stage 1: Build com Maven ─────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# Copia tudo do projeto
COPY . .

# Build da aplicação
RUN mvn clean package -DskipTests

# ── Stage 2: Imagem final enxuta ────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Usuário não-root
RUN addgroup -S spring && adduser -S spring -G spring

USER spring

# Copia o jar gerado
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Xms128m", "-Xmx400m", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]