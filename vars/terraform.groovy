def call() {
    pipeline {
        agent any

        parameters {
            string(name: 'ENV', defaultValue: '', description: 'Which Environment')
            string(name: 'ACTION', defaultValue: '', description: 'Which Action')
        }

        options {
            ansiColor('xterm')
        }

        stages {

            stage('init') {
                steps {
                    sh 'terraform init -backend-config=env-${ENV}/state.tfvars'
                }
            }

            stage('apply') {
                steps {
                    sh 'terraform apply -auto-approve -var-file=env-prod/main.tfvars'
                }
            }
        }
    }
}