FROM eclipse-temurin:21
WORKDIR /app
COPY build/libs/*.jar .
CMD java -jar *.jar

