pipeline {
    agent any

    options {
        skipDefaultCheckout(true)
    }

    environment {
        BASE_URL = 'https://practice.expandtesting.com/notes/app'
        API_BASE_URL = 'https://practice.expandtesting.com/notes/api'
        BROWSER = 'chrome'
        NOTES_EMAIL = credentials('NOTES_EMAIL')
        NOTES_PASSWORD = credentials('NOTES_PASSWORD')
        GEMINI_API_KEY = credentials('GEMINI_API_KEY')
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
