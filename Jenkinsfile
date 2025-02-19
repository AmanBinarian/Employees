pipeline {
    agent any
    environment {
        CODACY_API_TOKEN = credentials('codacy-token') 
        PROJECT_TOKEN = "ed311edadec7476c9463b5299dc44717"
        CODACY_URL = "https://app.codacy.com/api/v3/analysis"
    }
    stages {
        stage('Build') {
            steps {
                echo "Building the project..."
                sh 'mvn clean install -DskipTests' 
            }
        }
        stage('Run Tests') {
            steps {
                echo "Running tests and generating coverage report..."
                sh 'mvn test jacoco:report'  
            }
        }
        stage('Send Report to Codacy') {
            steps {
                echo "Sending coverage report to Codacy..."
                sh """
                curl -X POST -H "Content-Type: application/json" \
                     -H "api-token: $CODACY_API_TOKEN" \
                     -d '{
                           "projectToken": "$PROJECT_TOKEN",
                           "coverageReport": "target/site/jacoco/jacoco.xml"
                         }' \
                     "$CODACY_URL"
                """
            }
        }
    }
}
