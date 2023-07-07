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
                    sh 'cd aws-prameters; terraform init -backend-config=env-${ENV}/state.tfvars'
                    sh 'terraform init -backend-config=env-${ENV}/state.tfvars'
                }
            }

            stage('apply') {
                steps {
                    sh 'cd aws-parameters; terraform apply -auto-approve -var-file=env-${ENV}/main.tfvars'
                    sh 'terraform ${ACTION} -auto-approve -var-file=env-${ENV}/main.tfvars'
                }
            }
        }
    }
}