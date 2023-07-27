FROM amazoncorretto:17

# Set working directory
WORKDIR /app

COPY ./build/install/java-docker .

CMD ["./bin/java-docker"]