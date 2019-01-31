#!/usr/bin/env bash

set -e

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

#docker build -t "spring-cloud-docker/nacos-server" $DIR/../spring-cloud-docker/nacos-server
docker build -t "spring-cloud-docker/spring-nacos-server" $DIR/../spring-nacos-server
docker build -t "spring-cloud-docker/spring-nacos-client" $DIR/../spring-nacos-client
