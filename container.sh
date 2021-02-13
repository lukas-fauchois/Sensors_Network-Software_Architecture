#!/bin/bash

docker login -u vincentduboisdlc

echo
echo "Building Spring container"
cd spring
./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=algroupe2/spring -Dmaven.test.skip
docker tag algroupe2/spring vincentduboisdlc/algroup2-spring
docker push vincentduboisdlc/algroup2-spring
cd ..

echo
echo "Building Sensor container"
cd sensorSimulation
docker build -t algroupe2/sensor .
docker tag algroupe2/sensor vincentduboisdlc/algroup2-sensor
docker push vincentduboisdlc/algroup2-sensor
cd ..