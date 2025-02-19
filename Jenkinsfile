pipeline {
    agent any
 
    environment {
        GIT_REPO = 'https://github.com/AmanBinarian/Employee.git' 
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
                    sh 'mvn clean package -DskipTests' 
                }
            }
        }
 
        stage('Test') {
            steps {
                script {
                    echo "Running tests..."
                    sh 'mvn test'
                }
            }
        }
 
        stage('Codacy Analysis') {
            steps {
                script {
                    echo "Running Codacy Analysis..."
                    sh '''
                        export CODACY_PROJECT_TOKEN=${CODACY_PROJECT_TOKEN}
                        mvn clean compile codacy-analysis:coverage
                    '''
                }
            }
        }
 
        stage('Deploy') {
            steps {
                script {
                    echo "Deploying application..."
                    sh 'mvn deploy'  // Modify as needed
                }
            }
        }
    }
 
    post {
        success {
            echo "Build, Codacy analysis, and deployment successful!"
        }
        failure {
            echo "Pipeline failed! Check logs for details."
        }
    }
}
