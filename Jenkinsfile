pipeline {
    agent any
    environment {
        CODACY_API_TOKEN = credentials('codacy-token') 
    }
    stages {
        stage('Build') {
            steps {
                echo "Building the project..."
                bat 'mvn clean install -DskipTests' 
            }
        }
       
         stage('Send Report to Codacy') {
            steps {
                echo "Sending coverage report to Codacy..."
                bat """
            curl -X POST "https://app.codacy.com/api/v3/analysis/organizations/gh/AmanBinarian/repositories/Employees/issues/search" \
             -H "api-token: CODACY_API_TOKEN" \
             -H "Content-Type: application/json"

                """
            }
        }
    }
}
