Beyla Demo
----------

## Demo Applications

Create a Kubernetes cluster

```
kind create cluster --config=kind.yaml
```

Install the demo apps (Nginx, Java service, Rust service, Go service, load generator)

```
kubectl apply -f demo-apps.yaml
```

Port forward the nginx service

```
kubectl port-forward services/nginx 3333:3333
```

Generate load

```
./loadgen.sh
```

## Monitoring Backend

Install the monitoring backend

```
kubectl apply -f monitoring-backend.yaml
```

Port forward the Grafan UI

```
kubectl port-forward services/otel-lgtm 3000:3000
```

Use a Web browser to log in on [http://localhost:3000](http://localhost:3000), user _admin_ password _admin_.

## Beyla

```
kubectl apply -f beyla.yaml
```

## k6

```
kubectl apply -f k6/k6.yaml
```