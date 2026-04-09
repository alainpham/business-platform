kubectl create ns java-apps

kubectl apply -n  java-apps -f https://raw.githubusercontent.com/alainpham/business-platform/refs/heads/master/k8s-deployment-no-otel/smoke-test-deploy.yaml

kubectl apply -n  java-apps -f https://raw.githubusercontent.com/alainpham/business-platform/refs/heads/master/k8s-deployment-no-otel/hub-deploy.yaml
kubectl apply -n  java-apps -f https://raw.githubusercontent.com/alainpham/business-platform/refs/heads/master/k8s-deployment-no-otel/availability-calculator-deploy.yaml
kubectl apply -n  java-apps -f https://raw.githubusercontent.com/alainpham/business-platform/refs/heads/master/k8s-deployment-no-otel/notification-dispatcher-deploy.yaml
kubectl apply -n  java-apps -f https://raw.githubusercontent.com/alainpham/business-platform/refs/heads/master/k8s-deployment-no-otel/sms-deploy.yaml
kubectl apply -n  java-apps -f https://raw.githubusercontent.com/alainpham/business-platform/refs/heads/master/k8s-deployment-no-otel/email-deploy.yaml

kubectl apply -n  java-apps -f https://raw.githubusercontent.com/alainpham/business-platform/refs/heads/master/k8s-deployment-no-otel/k6.yaml
