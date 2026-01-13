# --- Estágio 1: Build (Compilação) ---
# Usamos uma imagem com Maven para compilar o projeto
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos apenas o pom.xml primeiro para aproveitar o cache de dependências
COPY pom.xml .
# Baixa as dependências (se o pom não mudou, o Docker usa o cache e é super rápido)
RUN mvn dependency:go-offline

# Copia o código fonte do projeto
COPY src ./src

# Compila e gera o JAR (pula testes para ser mais rápido no build do container)
RUN mvn clean package -DskipTests

# --- Estágio 2: Runtime (Execução) ---
# Usamos uma imagem leve apenas com o JRE para rodar
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copia o JAR gerado no estágio anterior para a imagem final
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta 8080
EXPOSE 8081

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]