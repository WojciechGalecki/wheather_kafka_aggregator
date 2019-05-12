#!/usr/bin/env bash
set -e

TAG=weather-service-v1

pushd ../../../ && ./gradlew --build-cache weather:bootJar && popd

cp ../../../weather/build/libs/weather*.jar weather.jar

docker build -t $TAG .

rm weather.jar
