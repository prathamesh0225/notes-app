pipeline {
    agent any

    environment {

        GEMINI_API_KEY =
            credentials(
                'gemini-api-key'
            )

        NOTES_EMAIL =
            credentials(
                'notes-email'
            )

        NOTES_PASSWORD =
            credentials(
                'notes-password'
            )
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/prathamesh0225/final-test-project.git'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Publish Allure') {

            steps {

                allure(
                    includeProperties: false,

                    jdk: '',

                    results: [[
                        path:
                        'target/allure-results'
                    ]]
                )
            }
        }

    }
}