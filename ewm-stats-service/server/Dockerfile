FROM amazoncorretto:11.0.2
COPY target/*.jar appStatsServer.jar
CMD ["java","-jar","/appStatsServer.jar", "--server.port=9090"]