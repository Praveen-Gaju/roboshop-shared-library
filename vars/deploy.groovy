def call() {
    pipeline {
        agent any

        parameters {
            string(name: 'app_version', defaultValue: '', description: 'App Version')
            string(name: 'component', defaultValue: '', description: 'component')
            string(name: 'environment', defaultValue: '', description: 'Environment')
        }

        stages {
            stage('update parameter store') {
                steps {
                    sh 'aws put-parameter --name ${environment}.${component}.app_version --type "String" --value "${app_version}" --overwrite'
                }
            }
            stage ('Deploy Servers') {
                steps {
                    sh 'aws ec2 describe-instances --filters "Name=tag:Name,Values=${component}-${environment}" --query "Reservations[*].Instances[*].PrivateIpAddress" --output text >/tmp/servers'
                    sh 'ansible-playbook -i /tmp/servers roboshop.yml -e role_name=${component} -e env=${environment}'
                }

            }
        }

        post {
            always {
                cleanWs()
            }
        }
    }


}