# Usa uma imagem base do Java para criar o ambiente
FROM openjdk:17-jdk-slim

# Define o diretorio de trabalho dentro do container
WORKDIR /app

# Copia o JAR da aplicacao para dentro do container
COPY target/estoqueFarma-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta que a aplicacao usa
EXPOSE 8080

# Comando para rodar a aplicacao
ENTRYPOINT ["java", "-jar", "app.jar"]