# Etapa 1: Build con Gradle
FROM gradle:8.4.0-jdk17 AS build

WORKDIR /app

# Copia archivos de configuración primero (para cache de dependencias)
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY gradlew ./

# Descarga dependencias (se cachea si no cambian)
RUN gradle dependencies --no-daemon || return 0

# Copia el código fuente
COPY src ./src

# Compila sin tests
RUN gradle clean build -x test --no-daemon

# Etapa 2: Runtime
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copia solo el JAR desde la etapa de build
COPY --from=build /app/build/libs/*.jar app.jar

# Expone el puerto (Render usa la variable $PORT)
EXPOSE 8080

# Ejecuta la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]