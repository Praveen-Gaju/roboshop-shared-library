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
    sh 'sonar-scanner -Dsonar.host.url=http://172.31.94.171:9000 -Dsonar.login=admin -Dsonar.password=admin123 -Dsonar.projectKey=${component} ${sonar_extra_opts}'
}