FROM java:8-jre


ENV SPRING_CLOUD_DOCKER_FILE target/spring-nacos-client-1.0-SNAPSHOT.jar

# Set the location of the verticles
ENV SPRING_CLOUD_DOCKER_HOME /opt/spring-cloud-docker

EXPOSE 8002

COPY $SPRING_CLOUD_DOCKER_FILE $SPRING_CLOUD_DOCKER_HOME/
#COPY src/config/docker.json $SPRING_CLOUD_DOCKER_HOME/


WORKDIR $SPRING_CLOUD_DOCKER_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["java    -jar spring-nacos-client-1.0-SNAPSHOT.jar"]