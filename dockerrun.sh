#!/bin/bash

mvn exec:exec@runoteld -f hub/pom.xml
mvn exec:exec@runoteld -f availability-calculator/pom.xml
mvn exec:exec@runoteld -f notification-dispatcher/pom.xml
mvn exec:exec@runoteld -f sms/pom.xml
mvn exec:exec@runoteld -f email/pom.xml

mvn exec:exec@runoteld -f smoke-test/pom.xml

docker run -d --rm \
  --name k6 \
  --network primenet \
  -v $(pwd)/k6/script.js:/config/script.js:ro \
  -v $(pwd)/k6/loop.sh:/home/k6/loop.sh:ro \
  --entrypoint /bin/sh \
  grafana/k6:latest-with-browser \
  /home/k6/loop.sh