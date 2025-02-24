pipeline {
    agent any
    environment {
        CODACY_API_TOKEN = credentials('codacy-token')
        GMAIL_APP_PASSWORD = credentials('app-password')
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
                try {
                    $jsonContent = Get-Content issues.json -Raw
                    if (-not $jsonContent) {
                        Write-Host "ERROR: issues.json is empty!"
                        exit 1
                    }

                    $json = $jsonContent | ConvertFrom-Json
                    if (-not $json.data) {
                        Write-Host "ERROR: JSON does not contain a 'data' property!"
                        exit 1
                    }

                    $output = @()
                    $errorCount = 0
                    $warningCount = 0

                    foreach ($issue in $json.data) {
                        $output += "Issue ID: $($issue.issueId)"
                        $output += "Message: $($issue.message)"
                        $output += "File Path: $($issue.filePath)"
                        $output += "Severity Level: $($issue.patternInfo.severityLevel)"
                        $output += "Sub Category: $($issue.patternInfo.subCategory)"
                        $output += "--------------------------------------"

                        if ($issue.patternInfo.severityLevel -eq "Error") {
                            $errorCount++
                        } elseif ($issue.patternInfo.severityLevel -eq "Warning") {
                            $warningCount++
                        }
                    }

                    # Save issue details
                    $output | Out-File -Encoding UTF8 codacy_issues.txt

                    # Save error & warning count
                    "$errorCount Errors`n$warningCount Warnings" | Out-File -Encoding UTF8 error_warning_count.txt

                   # Generate HTML file for Pie Chart
$htmlContent = @"
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
        google.charts.load('current', {'packages':['corechart']});
        google.charts.setOnLoadCallback(drawChart);

        function drawChart() {
            console.log('Google Charts Loaded...'); 

            var data = google.visualization.arrayToDataTable([
                ['Category', 'Count'],
                ['Errors', $errorCount],
                ['Warnings', $warningCount]
            ]);

            var options = {
                title: 'Error vs Warning Distribution',
                pieHole: 0.4,
                colors: ['#FF0000', '#FFA500'], // Red for Errors, Orange for Warnings
                legend: { position: 'bottom' }
            };

            var chart = new google.visualization.PieChart(document.getElementById('piechart'));
            chart.draw(data, options);
        }
    </script>
</head>
<body>
    <h2>Codacy Issues Report</h2>
    <p>Errors: $errorCount</p>
    <p>Warnings: $warningCount</p>
    <div id="piechart" style="width: 600px; height: 400px;"></div>
</body>
</html>
"@

$htmlContent | Out-File -Encoding UTF8 chart.html
              } 
                catch {
                    Write-Host "ERROR: Failed to parse JSON!"
                    Write-Host $_
                    exit 1
                }
                '''

                echo "Verifying Text Files..."
                bat "type codacy_issues.txt"
                bat "type error_warning_count.txt"
            }
        }

        stage('Send Email') {
            steps {
                powershell '''
                try {
                    $smtpServer = "smtp.gmail.com"
                    $smtpPort = 587
                    $smtpUser = "studyproject9821@gmail.com"
                    $smtpPass = $env:GMAIL_APP_PASSWORD

                    $from = "studyproject9821@gmail.com"
                    $to = "supradip.majumdar@binarysemantics.com"
                    $subject = "Codacy Issues Report"
                    $body = "Attached is the Codacy issues report with error and warning analysis.\n\nDownload the Html File to see the detailed report of Error and Warning in the Form of Pie Chart"

                    # Attachments
                    $attachments = @("codacy_issues.txt", "error_warning_count.txt", "chart.html")

                    # Create Mail Message Object
                    $message = New-Object System.Net.Mail.MailMessage
                    $message.From = $from
                    $message.To.Add($to)
                    $message.Subject = $subject
                    $message.Body = $body

                    foreach ($file in $attachments) {
                        $message.Attachments.Add((New-Object System.Net.Mail.Attachment($file)))
                    }

                    # Configure SMTP Client
                    $smtp = New-Object Net.Mail.SmtpClient($smtpServer, $smtpPort)
                    $smtp.EnableSsl = $true
                    $smtp.Credentials = New-Object System.Net.NetworkCredential($smtpUser, $smtpPass)

                    # Send Email
                    $smtp.Send($message)

                    Write-Host "Email sent successfully."
                }
                catch {
                    Write-Host "ERROR: Failed to send email."
                    Write-Host $_
                    exit 1
                }
                '''
            }
        }

        stage('Archive Reports') {
            steps {
                archiveArtifacts artifacts: 'codacy_issues.txt, issues.json, error_warning_count.txt, chart.html', fingerprint: true
            }
        }
    }
}
