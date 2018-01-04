pipeline {
	agent any
	stages {
	    stage('maven build image') {
	    	steps {
	        	sh 'docker build -t validation-service github.com/DeveloperHacker/Json-format-server'
	        }
	    }
	    stage('gradle build image') {
	    	steps {
		        sh './gradlew clean'
		        sh './gradlew build_image'
		    }
	    }
	}
}
