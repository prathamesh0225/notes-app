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

        stage('Run JMeter Test') {
            steps {
                bat '''
                if not exist performance\\results mkdir performance\\results

                del /f /q performance\\results\\result.jtl

                "%JMETER%\\jmeter.bat" -n ^
                -t performance/notes-performance.jmx ^
                -l performance/results/result.jtl
                '''
            }
        }

        stage('Generate JMeter HTML Report') {
            steps {
                bat '''
                if exist report rmdir /s /q report

                jmeter -g performance/results/result.jtl ^
                -o report
                '''
            }
        }

        stage('Publish Report') {
            steps {
                publishHTML([
                    reportDir: 'report',
                    reportFiles: 'index.html',
                    reportName: 'JMeter Report',
                    keepAll: true,
                    alwaysLinkToLastBuild: true,
                    allowMissing: true
                ])
            }
        }
    }

    post {
        always {

            echo 'Archiving screenshots...'
            archiveArtifacts artifacts: 'target/screenshots/**', allowEmptyArchive: true

            echo 'Archiving Allure results...'
            archiveArtifacts artifacts: 'allure-results/**', allowEmptyArchive: true

            echo 'Archiving surefire reports...'
            archiveArtifacts artifacts: 'target/surefire-reports/**', allowEmptyArchive: true

            junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true

            echo 'Archiving JMeter results and report...'
            archiveArtifacts artifacts: 'performance/results/*.jtl', allowEmptyArchive: true
            archiveArtifacts artifacts: 'report/**', allowEmptyArchive: true
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
