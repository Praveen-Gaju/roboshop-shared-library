def compile() {
    if(app_lang == "nodejs") {
        sh 'npm install'
    }

    if(app_lang == "maven"){
        sh 'mvn package'
    }
}

def testcases() {
   //npm test for nodejs
   //mvn test for maven
   //python -m unittests
   //go test for golang
   sh 'echo ok'
}

def codequality() {
    withAWSParameterStore(credentialsId: 'AWS_PARAM', naming: 'absolute', path: '/sonarqube', recursive: true, regionName: 'us-east-1') {
        sh "sonar-scanner -Dsonar.host.url=http://172.31.94.171:9000 -Dsonar.login=${SONARQUBE_USERNAME} -Dsonar.password=${SONARQUBE_PASSWORD} -Dsonar.projectKey=${component} -Dsonar.qualitygate.wait=true ${sonar_extra_opts}"
    }
}

def prepareArtifacts() {
    sh 'echo ${TAG_NAME} >VESION'
    //if (app_lang == "nodejs" || app_lang == "angular") {
        sh 'zip -r ${component}-${TAG_NAME}.zip * -x Jenkinsfile'
    //}
}

def artifactUpload() {
    sh 'echo ${TAG_NAME} >VESION'
    //if (app_lang == "nodejs" || app_lang == "angular") {
        sh 'curl -v -u admin:admin123 --upload-file ${component}-${TAG_NAME}.zip http://172.31.86.34:8081/repository/${component}/${component}-${TAG_NAME}.zip'
    //}
}