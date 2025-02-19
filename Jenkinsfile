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
                  
                    bat 'mvn clean compile codacy-analysis:run'
                   echo "Complete codacy "
            }
        }

        stage('Publish Codacy Report') {
            steps {
                echo "Publishing the report and generating pdf"
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

    post {
        success {
            echo "Build completed successfully!"
        }
        failure {
            echo "Build failed. Check the logs for errors."
        }
    }
}
