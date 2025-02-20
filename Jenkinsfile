pipeline {
    agent any
    environment {
        CODACY_API_TOKEN = credentials('codacy-token') 
        CODACY_URL = "https://app.codacy.com/api/v3/analysis/organizations/gh/AmanBinarian/repositories/Employees/issues/search"
    }
    stages {
        stage('Build') {
            steps {
                echo "Building the project..."
                bat 'mvn clean install -DskipTests' 
            }
        }
       
        stage('Fetch Codacy Issues') {
            steps {
                echo "Fetching issues from Codacy..."
                script {
                    def response = bat(
                        script: """
                        curl -X POST "${CODACY_URL}" ^
                            -H "api-token: ${CODACY_API_TOKEN}" ^
                            -H "Accept: application/json"
                        """,
                        returnStdout: true
                    ).trim()

                    echo "Codacy Response: ${response}"
                }
            }
        }
    }
}
