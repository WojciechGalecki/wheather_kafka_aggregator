#!/usr/bin/env bash
set -e

TAG=weather-data-producer-v1

pushd ../../../ && ./gradlew --build-cache weather-data-producer:bootJar && popd

cp ../../../weather-data-producer/build/libs/weather-data-producer*.jar weather-data-producer.jar

docker build -t $TAG .

rm weather-data-producer.jar
