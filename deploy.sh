#!/bin/bash
DEV_SERVER_CONTAINER_NAME=backend-server
DEV_DOCKER_IMAGE_NAME=hyeonseong1010/personal


# Stop & Remove Docker Container, If Container is running
if docker ps -a --format '{{.Names}}' | grep -q "^$DEV_SERVER_CONTAINER_NAME";
then
        sudo docker stop $DEV_SERVER_CONTAINER_NAME
        sudo docker remove $DEV_SERVER_CONTAINER_NAME
else
        echo "Server Container is not running"
fi

echo "Deployment Start..."

# Docker Image Remove, If Image is Exist
if docker images --format '{{.Repository}}:{{.Tag}}' | grep -q "$DEV_DOCKER_IMAGE_NAME.*latest";
then
        sudo docker rmi $DEV_DOCKER_IMAGE_NAME:latest
fi

# Pull Docker Image
sudo docker pull $DEV_DOCKER_IMAGE_NAME:latest

# Run Docker Container
sudo docker run -it --name $DEV_SERVER_CONTAINER_NAME -d -p 8080:8080 $DEV_DOCKER_IMAGE_NAME:latest

echo "Done!!"