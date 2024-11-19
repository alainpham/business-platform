# Video Streaming App

This README provides instructions to build and run the Video Streaming App using Docker.

# env vars for otel

```bash 
export OTEL_LOGS_EXPORTER="otlp"
export OTEL_TRACES_EXPORTER="otlp"
export OTEL_EXPORTER_OTLP_ENDPOINT="http://localhost:4318"
export OTEL_SERVICE_NAME="video-streaming-app"
export NODE_OPTIONS="--require @opentelemetry/auto-instrumentations-node/register"
```

## Prerequisites

- Docker installed on your machine
- Docker Compose installed on your machine

## Building the Docker Image

To build the Docker image for the Video Streaming App, run the following command:

```sh
docker build -t video-streaming-app .
```

## Running the Docker Container

To run the Docker container, use the following command:

```sh
docker run --rm -p 8080:8080 --name video-streaming-app video-streaming-app
```

## Stopping the Docker Container

To stop the running container, use the following command:

```sh
docker stop video-streaming-app
```

## Removing the Docker Container

To remove the stopped container, use the following command:

```sh
docker rm video-streaming-app
```