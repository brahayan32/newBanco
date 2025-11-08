# Usa una imagen base con Java
FROM eclipse-temurin:17-jdk

# Crea un directorio para la app
WORKDIR /app

# Copia el .jar generado al contenedor
COPY build/libs/banco-0.0.1-SNAPSHOT-plain.jar app.jar

# Expone el puerto que usa tu app (ajústalo si usas otro)
EXPOSE 8080

# Comando para ejecutar la app
CMD ["java", "-jar", "app.jar"]
