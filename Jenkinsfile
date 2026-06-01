pipeline {
    agent any

    environment {
        BASE_URL = 'https://practice.expandtesting.com/notes/app'
        API_BASE_URL = 'https://practice.expandtesting.com/notes/api'
        BROWSER = 'chrome'
        NOTES_EMAIL = credentials('email')
        NOTES_PASSWORD = credentials('password')
        GEMINI_API_KEY = credentials('gemini-api-key')
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/prathamesh0225/notes-app.git'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                catchError(buildResult: 'UNSTABLE', stageResult: 'FAILURE') {
                    bat 'mvn test'
                }
            }
        }

        stage('Publish Allure') {
            steps {
                allure(
                    includeProperties: false,
                    jdk: '',
                    results: [[path: 'target/allure-results']]
                )
            }
        }
    }

    post {
        always {
            echo 'Archiving Allure results...'
            archiveArtifacts artifacts: 'allure-results/**', allowEmptyArchive: true

            echo 'Archiving surefire reports...'
            archiveArtifacts artifacts: 'target/surefire-reports/**', allowEmptyArchive: true

            junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
            
            }

        success {
            echo 'Build passed'
        }

        unstable {
            echo 'Some tests failed'
        }

        failure {
            echo 'Build failed'
        }
    }
}
