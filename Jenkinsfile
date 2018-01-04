pipeline {
    tools {
        gradle "gradle-2.4"
    }
    agent {
        docker 'test_image'
    }
    stages {
        stage 'maven build image' {
            steps {
                sh 'docker build -t validation-service github.com/DeveloperHacker/Json-format-server'
            }
        }
        stage 'gradle build image' {
            steps {
                sh 'gradle build'
                sh './gradlew clean'
                sh './gradlew build_image'
                sh 'docker build -t validation-service github.com/DeveloperHacker/Json-format-server'
            }
        }
    }
}
