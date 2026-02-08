FROM eclipse-temurin:17.0.18_8-jre
WORKDIR /app
COPY inventory-service/build/libs/*.jar /app/app.jar
EXPOSE 8081 9091
ENTRYPOINT ["java","-jar","/app/app.jar"]
