#!/bin/bash
set -e
# JAVA
sudo apt install openjdk-17-jdk -y
java -version

# MVN
sudo apt install maven -y
mvn -version

# GIT
sudo apt install git -y
git --version

# MISC
sudo apt install ca-certificates curl gnupg lsb-release net-tools -y

# DOCKER
sudo mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt update
sudo apt install docker-ce docker-ce-cli containerd.io docker-compose-plugin -y
sudo usermod -aG docker $USER
newgrp docker
docker --version

# COMPOSE 
sudo apt install docker-compose -y
docker-compose --version

# MYSQL
sudo apt install mysql-server -y
sudo service mysql start
sudo service mysql status