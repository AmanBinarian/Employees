pipeline {
    agent any

    triggers {
        githubPush()  // Auto-triggers on GitHub push
    }

    stages {
        stage('Build & Test') {
            steps {
                bat 'mvn clean package'
            }
        }
    }
}
