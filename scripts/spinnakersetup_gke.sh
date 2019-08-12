#!/bin/sh

namespace=$1

echo " Listing all namespaces before creation of  $namespace "
echo "  "
kubectl --kubeconfig=config get namespaces 


echo " Creating namespace "
echo "  "
kubectl --kubeconfig=config create namespace $namespace

echo " Listing all namespaces after creation of  $namespace "
echo "  "
kubectl --kubeconfig=config get namespaces

echo " Creating halconfig from halconfigmap template "
echo "  "
kubectl --kubeconfig=config create -f halconfigmap_template.yml -n $namespace

echo " Creating kubeconfig from gkeconfig config file "
echo "  "
kubectl --kubeconfig=config create configmap kubeconfig --from-file=config -n $namespace

echo " Creating devconfig from devconfig config file "
echo "  "
kubectl --kubeconfig=config create configmap devconfig --from-file=devconfig -n $namespace

echo " Creating gcsconfig from gcs.json file"
echo "  "
kubectl --kubeconfig=config create configmap gcsconfig --from-file=gcs.json -n $namespace

echo " Listing all configmap from $namespace "
echo "  "
kubectl --kubeconfig=config get configmap -n $namespace

echo " Creating spin-halyard pod(deployment) from halyard template file "
echo "  "
kubectl --kubeconfig=config create -f halyard_template.yml -n $namespace

sleep 45
echo " Listing all the pods from $namespace "
echo "  "

kubectl --kubeconfig=config -n $namespace get pods

echo " Listing all from $namespace "
echo "  "

kubectl --kubeconfig=config -n $namespace get all
