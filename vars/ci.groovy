def call() {
   

    
    pipeline {
        agent any

        stages {

            stage('Compile/Build') {
                steps {
                    sh 'env'
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

    }
} 