FROM openjdk:8-jdk-alpine

MAINTAINER stephen(n10889299@qut.edu.au)

EXPOSE 8080

#ARG JAR_FILE=target/sp-gateway-0.0.1-SNAPSHOT.jar

RUN mkdir /home/java /home/java/lib
ADD target/sp-gateway-0.0.1-SNAPSHOT.jar /home/java/app.jar
ADD target/lib/* /home/java/lib/
ADD start.sh /home/java/start.sh
RUN chmod +x /home/java/start.sh

ENTRYPOINT ["/home/java/start.sh"]
