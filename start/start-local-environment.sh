#!/usr/bin/env bash
set -e

pushd ../docker/services/weather && ./build-image.sh
popd

docker-compose down
docker-compose up