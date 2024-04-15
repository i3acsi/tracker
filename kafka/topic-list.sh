#!/bin/bash

{
  source conf.properties
} &> /dev/null

/usr/local/kafka/bin/kafka-topics.sh --command-config conf.properties \
	--bootstrap-server ${servers} \
	--list
