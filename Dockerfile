# Stage 1: build do jar com Maven e JDK 21
FROM maven:3.9.4-eclipse-temurin-21-alpine AS build

WORKDIR /app

# Copia todos os arquivos para dentro do container
COPY . .

# Gera o jar, pulando testes (se quiser executar testes, tire o -DskipTests)
RUN ./mvnw clean package -DskipTests

# Stage 2: container runtime com JRE leve
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copia o jar gerado na etapa de build para este container
COPY --from=build /app/target/controle-verbas-backend-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta 8080
EXPOSE 8080

# Define profile de produção
ENV SPRING_PROFILES_ACTIVE=prod

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
