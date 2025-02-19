pipeline {
    agent any

    triggers {
        githubPush()  // Auto-triggers on GitHub push
    }

    stages {
        stage('Build & Test') {
            steps {
                 echo 'Deployment was successful!'
            }
        }
    }
}
