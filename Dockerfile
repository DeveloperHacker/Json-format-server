FROM gradle:alpine
ENV PORT 80
CMD java -jar /usr/src/app/target/json-format-server-jar-with-dependencies.jar
