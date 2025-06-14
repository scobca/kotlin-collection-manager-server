#!/bin/bash

mkdir /app

apt-get update && apt-get install -y git

cd /app

git clone https://github.com/scobca/kotlin-collection-manager-server.git .

cd ./collection-service

cp /home/.env ./.env

set -a
source .env
set +a

./gradlew build -x test

cd build/libs

chmod +x collection-service.jar

java -jar collection-service.jar