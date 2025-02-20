pipeline {
    agent any
    environment {
        CODACY_API_TOKEN = "v4NIo6JPf6DVYHY63J2i" 
    }
    stages {
        stage('Build') {
            steps {
                echo "Building the project..."
                bat 'mvn clean install -DskipTests' 
            }
        }
       
        stage('Fetch Codacy Issues & Save Report') {
            steps {
                echo "Fetching Codacy issues..."
                bat """
                curl -X GET "https://app.codacy.com/api/v3/analysis/organizations/gh/AmanBinarian/repositories/Employees/issues/search" ^
                     -H "api-token: %CODACY_API_TOKEN%" ^
                     -H "Content-Type: application/json" > issues.json
                """

                echo "Processing JSON data..."
                powershell '''
                $json = Get-Content issues.json | ConvertFrom-Json
                $output = @()
                foreach ($issue in $json.issues) {
                    $output += "Issue ID: $($issue.issueId)"
                    $output += "Message: $($issue.message)"
                    $output += "File Path: $($issue.filePath)"
                    $output += "Severity Level: $($issue.severityLevel)"
                    $output += "Sub Category: $($issue.subCategory)"
                    $output += "--------------------------------------"
                }
                $output | Out-File -Encoding UTF8 codacy_issues.txt
                '''
            }
        }
    }
}
