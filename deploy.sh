#!/bin/bash
echo ">>> Stopping existing containers..."
sudo docker-compose down

echo ">>> Removing old images..."
sudo docker image prune -f

echo ">>> Starting deployment..."
sudo docker-compose pull
sudo docker-compose up -d

echo "Done!!"