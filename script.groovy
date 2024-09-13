def buildJar() {
    echo "building the application..."
    sh 'mvn clean package'
} 

def buildImage() {
    echo "building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'dockerhub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh 'docker build -t mhmdelhamy/java-maven-app:${IMAGE_NAME} .'
        sh "echo $PASS | docker login -u $USER --password-stdin"
        sh 'docker push mhmdelhamy/java-maven-app:${IMAGE_NAME}'
    }
} 

def deployApp() {
    echo 'deploying the application...'
}

def versionUpdate() {
    echo 'commit version update...'
    withCredentials([usernamePassword(credentialsId: 'github-credentials', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        // git config here for the first time run
        sh 'git config --global user.email "jenkins@example.com"'
        sh 'git config --global user.name "jenkins"'
    
        sh "git remote set-url origin  https://$USER:$PASS@github.com/MuhamedElhamy/maven-app-jenkins.git"
        sh 'git add .'
        sh 'git commit -m "ci: version bump"'
        sh 'git push origin HEAD:master'
    }

}

return this
