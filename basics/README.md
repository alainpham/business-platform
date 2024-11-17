

```bash

docker network create --driver=bridge --subnet=172.20.0.0/16 --gateway=172.20.0.1 mainnet

docker run -d --name kafka --hostname kafka \
    --network mainnet \
    --ip 172.20.0.20 \
    -e KAFKA_CFG_NODE_ID=0 \
    -e KAFKA_CFG_PROCESS_ROLES=controller,broker \
    -e KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=0@kafka:9093 \
    -e KAFKA_CFG_LISTENERS=PLAINTEXT://172.20.0.20:9092,CONTROLLER://172.20.0.20:9093 \
    -e KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://172.20.0.20:9092 \
    -e KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT \
    -e KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER \
    -e KAFKA_CFG_INTER_BROKER_LISTENER_NAME=PLAINTEXT \
    bitnami/kafka:3.8.0

docker run -it --rm --network mainnet bitnami/kafka:3.8.0 kafka-topics.sh --list --bootstrap-server 172.20.0.20:9092
docker run -it --rm --network mainnet bitnami/kafka:3.8.0 kafka-console-producer.sh --bootstrap-server 172.20.0.20:9092 --topic quickstart-events
docker run -it --rm --network mainnet bitnami/kafka:3.8.0 kafka-console-consumer.sh --bootstrap-server 172.20.0.20:9092 --topic quickstart-events

docker rm -f kafka
```


```bash
mkdir -p /tmp/logs
echo '{"remote_addr": "192.168.1.1", "time_local": "14/Oct/2024:10:15:34 +0000", "request": "GET /index.html HTTP/1.1", "status": 200, "body_bytes_sent": 5120, "http_referer": "https://example.com", "http_user_agent": "Mozilla/5.0", "team": "ops"}' >> /tmp/logs/nginx_log.log
echo '{"remote_addr": "203.0.113.5", "time_local": "14/Oct/2024:11:12:08 +0000", "request": "POST /login HTTP/1.1", "status": 401, "body_bytes_sent": 320, "http_referer": "https://example.com/login", "http_user_agent": "curl/7.68.0", "team": "dev"}' >> /tmp/logs/nginx_log.log
echo '{"remote_addr": "198.51.100.3", "time_local": "14/Oct/2024:12:25:48 +0000", "request": "GET /images/logo.png HTTP/1.1", "status": 304, "body_bytes_sent": 0, "http_referer": "https://example.com/home", "http_user_agent": "Mozilla/5.0", "team": "ops"}' >> /tmp/logs/nginx_log.log
echo '{"remote_addr": "192.0.2.45", "time_local": "14/Oct/2024:13:40:22 +0000", "request": "GET /about HTTP/1.1", "status": 200, "body_bytes_sent": 1450, "http_referer": "https://google.com", "http_user_agent": "Safari/537.36", "team": "dev"}' >> /tmp/logs/nginx_log.log
echo '{"remote_addr": "203.0.113.17", "time_local": "14/Oct/2024:14:50:16 +0000", "request": "GET /contact HTTP/1.1", "status": 404, "body_bytes_sent": 380, "http_referer": "-", "http_user_agent": "Mozilla/5.0", "team": "ops"}' >> /tmp/logs/nginx_log.log
echo '{"remote_addr": "198.51.100.25", "time_local": "14/Oct/2024:15:05:32 +0000", "request": "GET /products HTTP/1.1", "status": 500, "body_bytes_sent": 0, "http_referer": "https://example.com/cart", "http_user_agent": "Mozilla/5.0", "team": "dev"}' >> /tmp/logs/nginx_log.log
echo '{"remote_addr": "192.0.2.75", "time_local": "14/Oct/2024:15:55:48 +0000", "request": "POST /api/update HTTP/1.1", "status": 200, "body_bytes_sent": 1220, "http_referer": "-", "http_user_agent": "PostmanRuntime/7.29.0", "team": "ops"}' >> /tmp/logs/nginx_log.log
echo '{"remote_addr": "203.0.113.30", "time_local": "14/Oct/2024:16:42:01 +0000", "request": "DELETE /api/user/45 HTTP/1.1", "status": 204, "body_bytes_sent": 0, "http_referer": "-", "http_user_agent": "Mozilla/5.0", "team": "dev"}' >> /tmp/logs/nginx_log.log
echo '{"remote_addr": "198.51.100.40", "time_local": "14/Oct/2024:17:10:28 +0000", "request": "PUT /api/order/123 HTTP/1.1", "status": 201, "body_bytes_sent": 460, "http_referer": "-", "http_user_agent": "curl/7.78.0", "team": "ops"}' >> /tmp/logs/nginx_log.log
echo '{"remote_addr": "192.0.2.55", "time_local": "14/Oct/2024:18:20:12 +0000", "request": "GET /blog/2024/10 HTTP/1.1", "status": 403, "body_bytes_sent": 112, "http_referer": "https://example.com/blog", "http_user_agent": "Mozilla/5.0", "team": "dev"}' >> /tmp/logs/nginx_log.log


```


```

local.file_match "tmplogs" {
  path_targets = [
    {__path__ = "/tmp/logs/*.log"},
  ]
}

loki.source.file "alain" {
    targets    = local.file_match.tmplogs.targets
    forward_to = [loki.process.json.receiver]
}

loki.process "json" {
    forward_to = [loki.write.grafana_cloud_loki.receiver]

    stage.json {
        expressions = {team = "team", status = "status", body_bytes_sent="body_bytes_sent"}
    }
    stage.labels {
        values = {
            team  = "team",        
            status = "status", 
        }
    }
    stage.structured_metadata {
        values = {
            body_bytes_sent = "body_bytes_sent",
        }
    }
    stage.metrics {
        metric.counter {
            name        = "body_bytes_sent_total"
            description = "nb of bytes sent"
            prefix      = "alain_"

            value   = "body_bytes_sent"
            action            = "add"
            max_idle_duration = "24h"
        }
    }
}

```



```
/*
loki.source.kafka "local" {
  brokers                = ["172.20.0.20:9092"]
  topics                 = ["quickstart-events"]
  labels                 = {component = "loki.source.kafka"}
  version                = "3.8.0"
 // forward_to             = [loki.relabel.kafka.receiver]
  forward_to             = [loki.write.grafana_cloud_loki.receiver]
}

loki.relabel "kafka" {
  forward_to      = [loki.write.grafana_cloud_loki.receiver]

  rule {
    source_labels = ["__meta_kafka_topic"]
    target_label  = "topic"
  }
}*/
```