# Usa una imagen base con Java y Gradle
FROM gradle:8.4.1-jdk17 AS build

# Copia el proyecto completo al contenedor
COPY . /app
WORKDIR /app

# Compila el proyecto sin ejecutar tests
RUN gradle build -x test

# Imagen final con solo el .jar
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copia el .jar generado desde la etapa anterior
COPY --from=build /app/build/libs/banco-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto
EXPOSE 8080

# Ejecuta la app
CMD ["java", "-jar", "app.jar"]
