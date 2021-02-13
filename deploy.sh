#!/bin/bash

#docker login
docker pull vincentduboisdlc/algroup2-spring
docker pull vincentduboisdlc/algroup2-sensor
docker stack deploy -c docker-compose.yaml algroupe2
#cat postgres/IoT.sql | docker exec -i postgres psql -Upifou IoT    #permet de peupler la bdd si spring.jpa.hibernate.ddl-auto=update dans spring>application.properties
docker stack services algroupe2
#docker stack rm algroupe2