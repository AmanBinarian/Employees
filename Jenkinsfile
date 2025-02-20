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
                curl -X POST "https://app.codacy.com/api/v3/analysis/organizations/gh/AmanBinarian/repositories/Employees/issues/search" ^
                     --header "api-token: %CODACY_API_TOKEN%" ^
                     --header "Content-Type: application/json" ^
                     --silent --show-error --fail ^
                     --output issues.json
                """

                echo "Checking JSON Content..."
                bat "type issues.json"

                echo "Processing JSON data..."
                powershell '''
                $jsonContent = Get-Content issues.json -Raw
                if (-not $jsonContent) {
                    Write-Host "ERROR: issues.json is empty!"
                    exit 1
                }

                try {
                    $json = $jsonContent | ConvertFrom-Json
                    if (-not $json.data) {
                        Write-Host "ERROR: JSON does not contain a 'data' property!"
                        exit 1
                    }

                    $output = @()
                    foreach ($issue in $json.data) {
                        $output += "Issue ID: $($issue.issueId)"
                        $output += "Message: $($issue.message)"
                        $output += "File Path: $($issue.filePath)"
                        $output += "Severity Level: $($issue.patternInfo.severityLevel)"
                        $output += "Sub Category: $($issue.patternInfo.subCategory)"
                        $output += "--------------------------------------"
                    }
                    $output | Out-File -Encoding UTF8 codacy_issues.txt
                }
                catch {
                    Write-Host "ERROR: Failed to parse JSON!"
                    Write-Host $_
                    exit 1
                }
                '''

                echo "Verifying Text File..."
                bat "type codacy_issues.txt"
            }
        }

      stage('Send Email') {
    steps {
        powershell '''
       $smtpServer = "smtp.gmail.com"
       $smtpPort = 587
       $smtpUser = "studyproject9821@gmail.com"
       $smtpPass = "jfob oxbo nsdd ilfz"

     $from = "studyproject9821@gmail.com"
     $to = "supradip.majumdar@binarysemantics.com"
     $subject = "Codacy Issues Report"
     $body = "Attached is the Codacy issues report."

    # Ensure the file path is set correctly
    $attachmentPath = "codacy_issues.txt"

    # Create Mail Message Object
    $message = New-Object System.Net.Mail.MailMessage
  $message.From = $from
  $message.To.Add($to)
  $message.Subject = $subject
  $message.Body = $body

     # Attach the file
    $message.Attachments.Add((New-Object System.Net.Mail.Attachment($attachmentPath)))

   # Configure SMTP Client
   $smtp = New-Object Net.Mail.SmtpClient($smtpServer, $smtpPort)
   $smtp.EnableSsl = $true
   $smtp.Credentials = New-Object System.Net.NetworkCredential($smtpUser, $smtpPass)

    # Send Email
   $smtp.Send($message)
        '''
    }
}

        
        stage('Archive Reports') {
            steps {
                archiveArtifacts artifacts: 'codacy_issues.txt, issues.json, codacy_issues.pdf', fingerprint: true
            }
        }
    }
}
