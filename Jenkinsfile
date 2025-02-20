pipeline {
    agent any
    environment {
        CODACY_API_TOKEN = credentials('codacy-token') 
        PROJECT_TOKEN = credentials('PROJECT_TOKEN')
        CODACY_URL = "https://app.codacy.com/api/v3/analysis"
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
                curl -X POST -H "Content-Type: application/json" \
                     -H "api-token: $CODACY_API_TOKEN" \
                     -d '{
                           "projectToken": "$PROJECT_TOKEN",
                           "coverageReport": "target/site/codacy/codacy_analysis.xml"
                         }' \
                     "$CODACY_URL"
                """
            }
        }
    }
}
