pipeline {
    agent any

    triggers {
        githubPush()  // Auto-triggers on GitHub push
    }
    
    stages {
        stage('Build & Test') {
            steps {
                echo "Starting Build..."
                bat 'mvn clean install -DskipTests'
            }
        }
    }

    post {
        success {
            echo "Build completed successfully!"
        }
        failure {
            echo "Build failed. Check the logs for errors."
        }
    }
}
