FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY build/libs/*.jar .
CMD java -jar *.jar

