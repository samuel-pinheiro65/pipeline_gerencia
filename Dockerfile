# ============================================
# Stage 1: Build da aplicação
# ============================================
FROM gradle:8.5-jdk21 AS build

WORKDIR /app

# Copiar arquivos de configuração do Gradle
COPY build.gradle settings.gradle ./
COPY gradle ./gradle

# Copiar Gradle Wrapper (necessário para executar gradlew)
COPY gradlew gradlew.bat ./

# Dar permissão de execução ao gradlew
RUN chmod +x ./gradlew

# Copiar código fonte
COPY src ./src

# Build da aplicação (sem testes para otimizar)
RUN ./gradlew build -x test --no-daemon

# ============================================
# Stage 2: Runtime
# ============================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Instalar wget para health check
RUN apk add --no-cache wget

# Criar usuário não-root para segurança
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiar JAR do stage de build
COPY --from=build /app/build/libs/*.jar app.jar

# Expor porta da aplicação
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Variáveis de ambiente
ENV JAVA_OPTS="-Xmx512m -Xms256m" \
    SPRING_PROFILES_ACTIVE=prod

# Comando para executar a aplicação
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

