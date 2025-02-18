pipeline {
    agent any
 
    environment {
        CODACY_API_TOKEN = credentials('codacy-token')
    }
 
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
 
        stage('Codacy Analysis') {
            steps {
                sh 'mvn org.codacy:codacy-maven-plugin:coverage -Dcodacy.projectToken=$CODACY_API_TOKEN'
            }
        }
 
        stage('Publish Codacy Report') {
            steps {
                publishHTML([allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'target/site',
                    reportFiles: 'index.html',
                    reportName: 'Codacy Report'
                ])
            }
        }
    }
}
