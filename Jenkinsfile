pipeline {
    agent any
 
    environment {
        GIT_REPO = 'https://AmanBinarian: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@github.com/AmanBinarian/Employees.git' 
        GIT_BRANCH = 'master' 
        CODACY_PROJECT_TOKEN = credentials('codacy-token') 
    }
 
    triggers {
        githubPush()  
    }
 
    stages {
        stage('Checkout Code') {
            steps {
                script {
                    echo "Triggered by GitHub Webhook!"
                    checkout scm
                }
            }
        }
 
        stage('Build') {
            steps {
                script {
                    echo "Building the Maven project..."
                }
            }
        }
 
        stage('Codacy Analysis') {
            steps {
                script {
                    echo "Running Codacy Analysis..."
                    bat 'set CODACY_PROJECT_TOKEN=%CODACY_PROJECT_TOKEN%'
                    echo "Set codacy token "
                    bat 'mvn clean install'
                    bat 'mvn clean compile codacy-analysis:coverage'
                
                }
            }
        }
    }  // 🔹 Closing bracket for `stages`

    post {  // 🔹 Move `post` outside of `stages`
        success {
            echo "Build, Codacy analysis, and deployment successful!"
        }
        failure {
            echo "Pipeline failed! Check logs for details."
        }
    }
}  // 🔹 Closing bracket for `pipeline`
