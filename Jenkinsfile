pipeline {
    agent any

    triggers {
        githubPush()  // Auto-triggers on GitHub push
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/AmanBinarian/Employees.git'
            }
        }

        stage('Build & Test') {
            steps {
                sh 'mvn clean package'
            }
        }
    }
}
