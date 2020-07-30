# ---- STAGE 1 - build stage ----
FROM adoptopenjdk/openjdk11 as builder
WORKDIR /build
ADD . .
RUN ./mvnw -e clean package
# ---- STAGE 2 - final image stage ----
FROM adoptopenjdk/openjdk11:jre-11.0.8_10
WORKDIR /app
COPY --from=builder ./build/target/demo-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","demo-0.0.1-SNAPSHOT.jar"]