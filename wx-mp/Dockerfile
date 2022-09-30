FROM openjdk:11
COPY ./build/libs/wx-mp-1.0-SNAPSHOT.jar /mydata/k8s/
WORKDIR /mydata/k8s/
ENTRYPOINT ["java", "-jar", "wx-mp-1.0-SNAPSHOT.jar"]
