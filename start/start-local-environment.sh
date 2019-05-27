#!/usr/bin/env bash
set -e

pushd ../docker/services/weather-data-producer && ./build-image.sh
popd

docker-compose down
docker-compose up