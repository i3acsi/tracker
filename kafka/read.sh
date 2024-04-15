#!/bin/bash

{
  source conf.properties
} &> /dev/null

/usr/local/kafka/bin/kafka-console-consumer.sh --consumer.config ./conf.properties --bootstrap-server ${servers} --timeout-ms 4500 --from-beginning --topic ${1}
