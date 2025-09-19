FROM maven:3.9.6-eclipse-temurin-17-alpine
ENV LOG_LEVEL=FINE
WORKDIR /app
COPY . .

CMD ["mvn", "clean", "test", "-Djava.version=17"]
