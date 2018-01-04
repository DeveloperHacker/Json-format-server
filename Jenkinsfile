pipeline {
	agent {}
	tools {
	    gradle "gradle-2.4"
	}
    stage 'maven build image' {
        sh 'docker build -t validation-service github.com/DeveloperHacker/Json-format-server'
    }
    stage 'gradle build image' {
        sh 'gradle build'
        sh './gradlew clean'
        sh './gradlew build_image'
        sh 'docker build -t validation-service github.com/DeveloperHacker/Json-format-server'
    }
}
