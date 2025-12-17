pipeline {
    agent any
    
    stages {
        // 1. Git Clone
        stage('📥 Git Clone') {
            steps {
                git branch: 'Tasnim', 
                    url: 'https://github.com/Tasnim847/Projet_Devops.git'
                sh 'echo "✅ Code source récupéré avec succès"'
            }
        }

        // 2. Vérification
        stage('🔍 Vérification') {
            steps {
                echo '🔍 Vérification des outils...'
                sh '''
                    ls -la
                    mvn --version
                    docker --version
                    java -version
                    kubectl version --client
                    echo "✅ Tous les outils sont installés"
                '''
            }
        }

        // 3. Build
        stage('🏗️ Build') {
            steps {
                echo '🏗️ Build de l\'application...'
                sh '''
                    mvn clean compile
                    echo "✅ Application compilée"
                '''
            }
        }

        // 4. Test
        stage('🧪 Tests') {
            steps {
                echo '🧪 Tests avec Base de Données...'
                sh '''
                    # Nettoyer d'abord
                    docker stop test-mysql 2>/dev/null || true
                    docker rm test-mysql 2>/dev/null || true
                    
                    # Démarrer MySQL pour tests
                    docker run -d \
                        --name test-mysql \
                        -e MYSQL_ROOT_PASSWORD=root123 \
                        -e MYSQL_DATABASE=springdb \
                        -p 3306:3306 \
                        mysql:8.0
                    
                    echo "⏳ Attente MySQL (30s)..."
                    sleep 30
                    
                    # Vérifier MySQL avec boucle
                    MAX_RETRIES=10
                    COUNTER=0
                    MYSQL_READY=false
                    
                    while [ $COUNTER -lt $MAX_RETRIES ]; do
                        COUNTER=$((COUNTER + 1))
                        
                        if docker exec test-mysql mysqladmin ping -h localhost -u root -proot123 2>/dev/null; then
                            echo "✅ MySQL prêt après $COUNTER tentatives"
                            MYSQL_READY=true
                            break
                        fi
                        
                        echo "⏳ Tentative $COUNTER/$MAX_RETRIES..."
                        sleep 5
                    done
                    
                    if [ "$MYSQL_READY" = "false" ]; then
                        echo "❌ MySQL non accessible après 50 secondes"
                        docker logs test-mysql --tail=20
                        exit 1
                    fi
                    
                    echo "✅ MySQL démarré - Exécution des tests..."
                    
                    # Exécuter tests
                    mvn test \
                        -Dspring.datasource.url=jdbc:mysql://localhost:3306/springdb \
                        -Dspring.datasource.username=root \
                        -Dspring.datasource.password=root123
                    
                    # Nettoyer
                    docker stop test-mysql
                    docker rm test-mysql
                '''
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                    sh 'echo "📊 Rapports de tests générés"'
                }
            }
        }

        // 5. Docker
        stage('🐳 Docker') {
            steps {
                echo '🐳 Construction image Docker...'
                sh '''
                    # Package JAR
                    mvn clean package -DskipTests
                    
                    # Build Docker
                    docker build -t tasnim847/student-app:1.0.4 .
                    echo "✅ Image Docker créée"
                '''
            }
        }
        
        // 6. Push Docker
        stage('🚀 Push Docker') {
            steps {
                echo '🚀 Push vers DockerHub...'
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub_creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push tasnim847/student-app:1.0.4
                        echo "✅ Image poussée vers DockerHub"
                    '''
                }
            }
        }

        // 7. SonarQube
        stage('🔍 SonarQube') {
            steps {
                echo '🔍 Analyse SonarQube...'
                withCredentials([string(credentialsId: 'jenkins_sonar', variable: 'SONAR_TOKEN')]) {
                    sh '''
                        echo "🔍 Test connexion SonarQube..."
                        if curl -f --max-time 30 http://192.168.217.135:9000; then
                            echo "✅ SonarQube accessible"
                        else
                            echo "❌ SonarQube non accessible"
                            exit 1
                        fi
                        
                        echo "🔍 Démarrage analyse SonarQube..."
                        mvn sonar:sonar \
                            -Dsonar.projectKey=Devops \
                            -Dsonar.host.url=http://192.168.217.135:9000 \
                            -Dsonar.login=$SONAR_TOKEN
                        
                        echo "✅ Analyse SonarQube terminée"
                    '''
                }
            }
        }

        // 8. Kubernetes 
        stage('🚀 Déploiement Kubernetes') {
            steps {
                echo '🚀 Mise à jour du déploiement Spring Boot sur le cluster...'
                sh '''
                    echo "🔍 Vérification de l'accès au cluster..."
                    kubectl config current-context
                    kubectl get nodes
                    
                    echo "📁 Mise à jour de l'image du déploiement Spring..."
                    # Met à jour l'image du déploiement avec la nouvelle version
                    kubectl set image deployment/spring-app spring-app=tasnim847/student-app:1.0.4 -n devops
                    
                    # Vérifie que le rollout se déroule correctement
                    echo "🔄 Vérification du déploiement..."
                    kubectl rollout status deployment/spring-app -n devops --timeout=180s
                    
                    echo "✅ Déploiement Kubernetes terminé !"
                    echo "🎯 Nouvelle version déployée : tasnim847/student-app:1.0.4"
                '''
            }
        }
        
        // 9. Archive JAR
        stage('📦 Archive JAR') {
            steps {
                echo '📦 Archivage du fichier JAR...'
                sh '''
                    echo "📁 Contenu du dossier target:"
                    ls -la target/
                    
                    echo "📦 Archivage du JAR..."
                    JAR_FILE=$(ls target/*.jar | head -1)
                    
                    if [ -f "$JAR_FILE" ]; then
                        echo "✅ Fichier JAR trouvé: $JAR_FILE"
                        echo "📏 Taille du JAR: $(du -h $JAR_FILE | cut -f1)"
                        echo "🔢 Version: 1.0.4"
                        
                        # Créer un dossier d'archivage
                        mkdir -p archives
                        cp $JAR_FILE archives/student-management-1.0.4.jar
                        
                        # Créer un fichier d'information
                        echo "Application: Student Management" > archives/build-info.txt
                        echo "Version: 1.0.4" >> archives/build-info.txt
                        echo "Date: $(date)" >> archives/build-info.txt
                        echo "Build: $BUILD_NUMBER" >> archives/build-info.txt
                        echo "Image Docker: tasnim847/student-app:1.0.4" >> archives/build-info.txt
                        echo "URL SonarQube: http://192.168.217.135:9000/dashboard?id=Devops" >> archives/build-info.txt
                        
                        echo "✅ Archivage terminé"
                    else
                        echo "❌ Aucun fichier JAR trouvé dans target/"
                        exit 1
                    fi
                '''
            }
        }
    }
    
    post {
        always {
            echo '📊 Pipeline terminé'
            sh '''
                echo "🧹 Nettoyage..."
                docker stop test-mysql 2>/dev/null || true
                docker rm test-mysql 2>/dev/null || true
                
                # Arrêter le port-forward si toujours actif
                pkill -f "kubectl port-forward" 2>/dev/null || true
            '''
        }
        success {
            echo '🎉 SUCCÈS! Toutes les étapes validées!'
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            archiveArtifacts artifacts: 'archives/**', fingerprint: true
            
            sh '''
                echo "📢 DÉPLOIEMENT COMPLET RÉUSSI!"
                echo "=========================================="
                MINIKUBE_IP=$(minikube ip 2>/dev/null || echo "192.168.49.2")
                echo "🌐 Application: http://$MINIKUBE_IP:30080/student"
                echo "🐳 Docker Image: tasnim847/student-app:1.0.4"
                echo "📊 SonarQube: http://192.168.217.135:9000/dashboard?id=Devops"
                echo "📦 JAR Archive: archives/student-management-1.0.4.jar"
                echo "📋 Build Info: archives/build-info.txt"
                echo "=========================================="
            '''
        }
        failure {
            echo '❌ ÉCHEC! Pipeline a échoué.'
            sh '''
                echo "🔍 Derniers logs Kubernetes:"
                kubectl get events -n devops --sort-by='.lastTimestamp' 2>/dev/null | tail -10 || echo "⚠️ Impossible de récupérer les logs"
                
                echo "🔍 État des pods:"
                kubectl get pods -n devops 2>/dev/null || true
            '''
        }
        unstable {
            echo '⚠️ Pipeline instable!'
        }
        changed {
            echo '📈 Pipeline changé depuis la dernière exécution!'
        }
    }
}