FROM openjdk:11-jdk
EXPOSE 8080
COPY target/weather-history.war weather-history-docker.war
CMD ["java","-jar","/weather-history.war"]