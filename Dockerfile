FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

COPY gradlew ./
COPY gradle ./gradle
COPY settings.gradle.kts build.gradle.kts gradle.properties ./
RUN chmod +x ./gradlew && ./gradlew --version

COPY src ./src
RUN ./gradlew --no-daemon installDist

FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app
COPY --from=build /workspace/build/install/ac-api ./

EXPOSE 8080

RUN groupadd --system app && useradd --system --gid app app && chown -R app:app /app
USER app

ENTRYPOINT ["/app/bin/ac-api"]
