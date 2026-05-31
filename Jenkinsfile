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

        stage('Generate JMeter HTML Report') {
            steps {
                catchError(buildResult: 'UNSTABLE', stageResult: 'FAILURE') {
                    bat '''
                    if exist report rmdir /s /q report
                    if exist performance\\results\\result.jtl ( jmeter -g performance/results/result.jtl -o report) 
                    else (echo "JTL file not found - skipping report generation")
                    '''
                }
            }
        }

        stage('Publish Allure') {

            steps {

                allure(
                    includeProperties: false,

                    jdk: '',

                    results: [[path:'target/allure-results']]
                )
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
            archiveArtifacts(artifacts: 'target/screenshots/**', allowEmptyArchive: true)

            echo 'Archiving Allure results...'
            archiveArtifacts(artifacts: 'allure-results/**',allowEmptyArchive: true)

            echo 'Archiving surefire reports...'
            archiveArtifacts(artifacts: 'target/surefire-reports/**',allowEmptyArchive: true)

            junit(testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true)

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

