
- [business platform demo](#business-platform-demo)
  - [architecture diagram](#architecture-diagram)
  - [build services and run using docker](#build-services-and-run-using-docker)
    - [build docker locally](#build-docker-locally)
    - [run local container in background with otel](#run-local-container-in-background-with-otel)
    - [build and run local with otel for testing](#build-and-run-local-with-otel-for-testing)
  - [build and push to dockerhub](#build-and-push-to-dockerhub)
  - [build and run k6/cron image](#build-and-run-k6cron-image)
    - [build with docker](#build-with-docker)
    - [run k6 test](#run-k6-test)

# business platform demo

This is a guide to deploy a set of synchronous and asynchronous microservices in spring boot to observed with & Grafana LGTM OSS stack or Grafana Cloud.

## architecture diagram
![alt text](graphics/architecture.png)


## build services and run using docker

### build docker locally
```bash
mvn install
mvn clean package exec:exec@rmi exec:exec@build -f availability-calculator/pom.xml
mvn clean package exec:exec@rmi exec:exec@build -f notification-dispatcher/pom.xml
mvn clean package exec:exec@rmi exec:exec@build -f hub/pom.xml
```

### run local container in background with otel
```bash
mvn exec:exec@runoteld -f availability-calculator/pom.xml
mvn exec:exec@runoteld -f notification-dispatcher/pom.xml
mvn exec:exec@runoteld -f hub/pom.xml
```

### build and run local with otel for testing
```bash
mvn install
mvn clean package exec:exec@rmi exec:exec@build exec:exec@runotel -f availability-calculator/pom.xml
mvn clean package exec:exec@rmi exec:exec@build exec:exec@runotel -f hub/pom.xml

```

## build and push to dockerhub

```bash
mvn install
mvn clean package exec:exec@buildpush -f availability-calculator/pom.xml
mvn clean package exec:exec@buildpush -f hub/pom.xml
```

## build and run k6/cron image

### build with docker
```bash
docker rmi alainpham/k6cron:latest
docker build -t alainpham/k6cron:latest -f k6/Dockerfile k6

docker push alainpham/k6cron:latest
```

### run k6 test
```bash
docker run -d --rm \
  --name k6 \
  --network primenet \
  -v $(pwd)/k6/script.js:/config/script.js:ro \
  -v $(pwd)/k6/loop.sh:/home/k6/loop.sh:ro \
  --entrypoint /bin/sh \
  grafana/k6:1.0.0-with-browser \
  /home/k6/loop.sh
```