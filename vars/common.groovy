def compile() {
    if(app_lang == "nodejs") {
        sh 'npm install'
    }

    if(app_lang == "maven"){
        sh 'mvn package ; mv target/${component}-1.0.jar ${component}.jar'
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

    //This code is for using nexus
//    sh 'echo ${TAG_NAME} >VERSION'
//    if (app_lang == "nodejs" || app_lang == "angular" || app_lang == "golang" || app_lang == "python") {
//        sh 'zip -r ${component}-${TAG_NAME}.zip * -x Jenkinsfile'
//    }
//    if (app_lang == "maven") {
//        sh 'zip -r ${component}-${TAG_NAME}.zip ${component}.jar schema VERSION'
//    }
    //for using ECR
    sh 'docker build -t 699776063346.dkr.ecr.us-east-1.amazonaws.com/${component}:${TAG_NAME} .'
}

def artifactUpload() {
   // artifact upload using nexus
//    env.NEXUS_USER = sh ( script: 'aws ssm get-parameter --name prod.nexus.username --with-decryption | jq .Parameter.Value | xargs', returnStdout: true ).trim()
//    env.NEXUS_PASS = sh ( script: 'aws ssm get-parameter --name prod.nexus.password --with-decryption | jq .Parameter.Value | xargs', returnStdout: true ).trim()
//
//    wrap([$class: 'MaskPasswordsBuildWrapper',
//          varPasswordPairs: [[password: NEXUS_USER], [password: NEXUS_PASS]]]) {
//
//        sh 'echo ${TAG_NAME} >VERSION'
//        //if (app_lang == "nodejs" || app_lang == "angular") {
//        sh 'curl -v -u ${NEXUS_USER}:${NEXUS_PASS} --upload-file ${component}-${TAG_NAME}.zip http://172.31.86.34:8081/repository/${component}/${component}-${TAG_NAME}.zip'
//        //}
//    }
    //artifact upload using ECR

    sh 'aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 699776063346.dkr.ecr.us-east-1.amazonaws.com'
    sh 'docker push 699776063346.dkr.ecr.us-east-1.amazonaws.com/${component}:${TAG_NAME}'

}