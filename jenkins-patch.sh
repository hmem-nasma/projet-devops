#!/bin/bash
echo "=== NETTOYAGE COMPLET JENKINS ==="

# 1. Stop et supprime tout Jenkins
echo "1. Arrêt des conteneurs Jenkins..."
sudo docker stop jenkins 2>/dev/null
sudo docker rm jenkins 2>/dev/null

# 2. Supprime tous les volumes Jenkins
echo "2. Suppression des volumes..."
sudo docker volume rm -f jenkins_clean_fresh 2>/dev/null || true

# 3. Vérifie qu'aucun volume ne reste
echo "3. Vérification..."
VOLUMES=$(sudo docker volume ls | grep -i jenkins)
if [ -n "$VOLUMES" ]; then
    echo "❌ Volumes restants:"
    echo "$VOLUMES"
    echo "Suppression manuelle nécessaire..."
    echo "sudo docker volume ls | grep jenkins | awk '{print \$2}' | xargs sudo docker volume rm -f"
else
    echo "✅ Aucun volume Jenkins trouvé"
fi

# 4. Lance un NOUVEAU Jenkins avec NOUVEAU volume
echo "4. Nouvelle installation..."
sudo docker run -d \
  --name jenkins \
  -e JAVA_OPTS="-Dhudson.model.DownloadService.noSignatureCheck=true" \
  -p 8080:8080 \
  -p 50000:50000 \
  -v jenkins_clean_fresh:/var/jenkins_home \
  jenkins/jenkins:lts

# 5. Attends
echo "Attente du démarrage (20 secondes)..."
sleep 20

# 6. Récupère le NOUVEAU mot de passe
echo "5. Récupération du mot de passe..."
PASSWORD=$(sudo docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword 2>/dev/null)

if [ -n "$PASSWORD" ]; then
    echo "✅ SUCCÈS COMPLET !"
    echo "=================================="
    echo "JENKINS TOUT NEUF !"
    echo "URL: http://localhost:8080"
    echo "MOT DE PASSE INITIAL:"
    echo "$PASSWORD"
    echo "=================================="
    echo "Cette fois tu devrais voir:"
    echo "1. Écran de déverrouillage avec le mot de passe"
    echo "2. Puis installation des plugins"
    echo "3. Puis CRÉATION d'un admin user"
else
    echo "❌ Échec. Vérifiez:"
    sudo docker logs jenkins --tail 30
fi