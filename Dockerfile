# =============================================
# Dockerfile para deploy no Render
# =============================================

# Etapa 1: Build da aplicação com Maven
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Compila e gera o JAR pulando os testes (testes rodam no CI)
RUN mvn clean package -DskipTests

# Etapa 2: Imagem final menor apenas com o JRE
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta 8080 (padrão do Spring Boot)
EXPOSE 8080

# Roda a aplicação com o perfil de produção
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]
