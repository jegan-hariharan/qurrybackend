pipeline { 
    environment { 
        registry = "jegansanthosh/backendq" 
        registryCredential = 'Dockerhub_jegan' 
       
    }
    agent any 
    stages { 
        stage('Building our image') { 
            steps { 
                  
        
                sh 'docker build --network host -t qurrybackend:$BUILD_NUMBER .'
                sh 'docker tag qurrybackend:$BUILD_NUMBER jegansanthosh/backendq:$BUILD_NUMBER'
            } 
        }
        stage('push our image') { 
            steps { 
                script { 
                    docker.withRegistry( '', registryCredential ) { 
                        sh 'docker push jegansanthosh/backendq:$BUILD_NUMBER' 
                    }
                } 
            }
        }   
    }
}
