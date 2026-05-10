FROM eclipse-temurin:17.0.19_10-jre
WORKDIR /app
COPY placeorder-service/build/libs/*.jar /app/app.jar
EXPOSE 8080 9090
ENTRYPOINT ["java","-jar","/app/app.jar"]
