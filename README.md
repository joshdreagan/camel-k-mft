# Camel K MFT Demo

## Create the requisite ConfigMap and Secret objects

```
# Don't forget to update the files with your personal credentials/keys/information.

oc create secret generic aws2-s3-credentials --from-file=aws-secret.properties
oc create secret generic sftp-credentials --from-file=sftp-secret.properties
oc create configmap camel-k-mft-properties --from-file=application.properties
```

## Deploy the Camel K integration

```
kamel run --config configmap:camel-k-mft-properties --config secret:sftp-credentials --config secret:aws2-s3-credentials SftpGateway.java
```

## Listen on the 'files' Kafka topic for AWS S3 download links

```
oc run kafka-consumer -ti --image=registry.redhat.io/amq7/amq-streams-kafka-28-rhel8:1.8.0 --rm=true --restart=Never -- bin/kafka-console-consumer.sh --bootstrap-server my-cluster-kafka-bootstrap:9092 --topic files --from-beginning
```
