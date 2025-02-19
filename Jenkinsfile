pipeline {
    agent any

    triggers {
        githubPush()  // Auto-triggers on GitHub push
    }

    tools {
        maven 'Maven'  // Ensure Maven is installed and configured
    }

    stages {
        stage('Build & Test') {
            steps {
                echo "Starting Build..."
                bat 'mvn clean -DskipTests'
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
