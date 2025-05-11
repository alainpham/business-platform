
- [business platform demo](#business-platform-demo)
  - [architecture diagram](#architecture-diagram)
  - [Build and push to dockerhub](#build-and-push-to-dockerhub)
  - [Build run local](#build-run-local)
  - [Run local container](#run-local-container)


# business platform demo

This is a guide to deploy a set of synchronous and asynchronous microservices in spring boot to observed with & Grafana LGTM OSS stack or Grafana Cloud.

## architecture diagram
![alt text](graphics/architecture.png)

## Build and push to dockerhub

```bash
mvn install
mvn package exec:exec@buildpush -f availability-calculator/pom.xml
mvn package exec:exec@buildpush -f hub/pom.xml
```

## Build run local
```bash
mvn install
mvn package exec:exec@rmi exec:exec@build exec:exec@runotel -f availability-calculator/pom.xml
```

## Run local container
```bash
mvn exec:exec@run -f availability-calculator/pom.xml
```