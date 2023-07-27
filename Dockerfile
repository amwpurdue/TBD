FROM amazoncorretto:17

# Set working directory
WORKDIR /app

COPY ./build/install/java-docker .

EXPOSE 5000
CMD ["./bin/java-docker"]