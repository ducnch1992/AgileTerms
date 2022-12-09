FROM openjdk:18.0.2.1-jdk-bullseye as build_img
WORKDIR /usr/src
COPY ./ ./agile
RUN apt update -y && apt install maven -y
RUN cd ./agile/ && mvn -P test package -DskipTests

FROM openjdk:18.0.2.1-jdk-bullseye
WORKDIR /usr/src/app
COPY --from=build_img /usr/src/agile/target/agileterm-0.0.1-SNAPSHOT.jar agileterm-spring
CMD ["java","-jar","agileterm-spring"]
EXPOSE 8080