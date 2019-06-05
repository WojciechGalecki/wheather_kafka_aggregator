#!/usr/bin/env bash
set -e

TAG=weather-data-processor-v1

pushd ../../../ && ./gradlew --build-cache weather-data-processor:bootJar && popd

cp ../../../weather-data-processor/build/libs/weather-data-processor*.jar weather-data-processor.jar

docker build -t $TAG .

rm weather-data-processor.jar
