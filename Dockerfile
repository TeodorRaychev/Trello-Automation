FROM maven:3.9.6-eclipse-temurin-17-alpine
ENV LOG_LEVEL=FINE \
SUITE_FILE=full_suite.xml

WORKDIR /app
COPY . .

CMD ["mvn", "clean", "test", "-Djava.version=17", "-DsuiteXmlFile=${SUITE_FILE}"]
