FROM maven:3.3-jdk-8-onbuild
ENV PORT 80
CMD java -jar /usr/src/app/target/json-format-server-jar-with-dependencies.jar
