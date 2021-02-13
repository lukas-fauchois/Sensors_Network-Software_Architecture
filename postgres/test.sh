#!/bin/bash

docker run -p 5432:5432 -d \
    --name postgres \
    -e POSTGRES_USER=pifou \
    -e POSTGRES_PASSWORD=pasglop \
    -e POSTGRES_DB=IoT \
    postgres

#docker exec -it postgres psql -U pifou IoT
#\dt