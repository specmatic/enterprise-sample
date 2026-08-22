FROM eclipse-temurin:25.0.4_7-jre
WORKDIR /app
COPY placeorder-service/build/libs/*.jar /app/app.jar
EXPOSE 8080 9090
ENTRYPOINT ["java","-jar","/app/app.jar"]
