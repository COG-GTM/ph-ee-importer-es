FROM eclipse-temurin:21-jre
WORKDIR /app
COPY build/libs/*.jar .
CMD java -jar *.jar

