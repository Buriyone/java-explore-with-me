FROM amazoncorretto:11.0.2
COPY target/*.jar appMainService.jar
CMD ["java","-jar","/appMainService.jar", "--server.port=8080"]