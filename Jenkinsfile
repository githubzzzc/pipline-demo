pipeline {
    agent any

    stages {
        stage('Hello') {
            steps {
                echo 'Hello World'
            }
        }
        stage('pull code') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[credentialsId: '56246d56-eb94-4c91-91fc-009d7ec5242a', url: 'git@github.com:githubzzzc/api_code.git']]])
            }
        }
        stage('build') {
            steps {
                sh 'mvn clean package'
            }
        }
    }
}
