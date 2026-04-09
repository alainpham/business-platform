kubectl create ns java-apps

kubectl apply -n  java-apps -f https://raw.githubusercontent.com/alainpham/business-platform/refs/heads/master/k8s-deployment/smoke-test-deploy.yaml

kubectl apply -n  java-apps -f https://raw.githubusercontent.com/alainpham/business-platform/refs/heads/master/k8s-deployment/hub-deploy.yaml
kubectl apply -n  java-apps -f https://raw.githubusercontent.com/alainpham/business-platform/refs/heads/master/k8s-deployment/availability-calculator-deploy.yaml
kubectl apply -n  java-apps -f https://raw.githubusercontent.com/alainpham/business-platform/refs/heads/master/k8s-deployment/notification-dispatcher-deploy.yaml
kubectl apply -n  java-apps -f https://raw.githubusercontent.com/alainpham/business-platform/refs/heads/master/k8s-deployment/sms-deploy.yaml
kubectl apply -n  java-apps -f https://raw.githubusercontent.com/alainpham/business-platform/refs/heads/master/k8s-deployment/email-deploy.yaml

kubectl apply -n  java-apps -f https://raw.githubusercontent.com/alainpham/business-platform/refs/heads/master/k8s-deployment/k6.yaml
