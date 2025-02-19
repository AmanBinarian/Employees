pipeline {
    agent any

    triggers {
        githubPush()  // Auto-triggers on GitHub push
    }

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/AmanBinarian/Employee'
            }
        }
    }
}
   
