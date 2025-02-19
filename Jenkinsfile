pipeline {
    agent any

    environment {
        GIT_CREDENTIALS = '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' 
    }

    stages {
        stage('Checkout Code') {
            steps {
                script {
                    sh 'git clone https://${GIT_CREDENTIALS}@github.com/AmanBinarian/Employee.git'
                }
            }
        }
    }
}
