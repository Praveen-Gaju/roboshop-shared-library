def call() {
    if(!env.sonar_extra_opts) {
        env.sonar_extra_opts=""
    }
    
    pipeline {
        agent any

        stages {

            stage('Compile/Build') {
                steps {
                    sh 'exit 1'
                    script {
                        common.compile()
                    }
                }
            }

            stage('Test Cases') {
                steps {
                    script {
                        common.testcases()
                    }
                }
            }

            stage('Code Quality') {
                steps {
                    script {
                        common.codequality()
                    }
                }
            }
        }

        post {
            failure {
                mail bcc: '', body: "${component} - Pipe Line Failed /n ${BUILD_URL}", cc: '', from: 'praveen.gaju94@gmail.com', replyTo: '', subject: "${component} - Pipe Line Failed", to: 'praveen.gaju94@gmail.com'
            }
        }
    }
} 