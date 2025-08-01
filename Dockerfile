# Usa imagem leve com JDK 21 (compatível com seu Java)
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copia o jar gerado pelo Maven para o container
COPY target/controle-verbas-backend-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta padrão
EXPOSE 8080

# Define a variável de ambiente para o profile de produção
ENV SPRING_PROFILES_ACTIVE=prod

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
