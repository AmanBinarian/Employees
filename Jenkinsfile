pipeline {
    agent any
    
    environment {
        CODACY_PROJECT_TOKEN = credentials('codacy-token') 
    }
 
    triggers {
        githubPush()  // Auto-triggers on GitHub push
    }
    
    stages {
        stage('Build') {
            steps {
                echo "Starting Build..."
                bat 'mvn clean install -DskipTests'
            }
        }
        stage('CodacyReportGeneration') {
            steps {
                echo "Starting codacy report..."
                
                bat 'set CODACY_PROJECT_TOKEN=%CODACY_PROJECT_TOKEN%'
                    echo "Set codacy token "
                  
                    bat 'mvn clean compile codacy-analysis:coverage'
                   echo "Complete codacy "
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
