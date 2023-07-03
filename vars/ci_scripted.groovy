def call() {
    if (!env.sonar_extra_opts) {
        env.sonar_extra_opts = ""
    }

    if (env.TAG_NAME ==~ ".*") {
        env.GTAG = "true"
    } else {
        env.GTAG = "false"
    }

    node('workstation') {

        try {

            stage ('Check Out Code'){
                cleanWs()
                git branch: 'main', url: "https://github.com/Praveen-Gaju/${component}"
            }

            sh 'env'


            if (env.BRANCH_NAME != "main"){
                stage('Compile/Build') {
                    common.compile()
                }
            }

            if(env.GTAG != "true" && env.BRANCH_NAME != "main") {
                stage('Test Cases') {
                    common.testcases()
                }
            }

            if(env.BRANCH_NAME ==~ "PR-.*") {
                stage('Code Quality') {
                    common.codequality()
                }
            }

            if(env.GTAG == "true") {
                stage('Package') {
                    common.prepareArtifacts()
                }
                stage('Artifact Upload') {
                    common.testcases()
                }
            }


        } catch (e) {
            mail bcc: '', body: "<h1>${component} - Pipe Line Failed \n ${BUILD_URL}</h1>", cc: '', from: 'praveen.gaju94@gmail.com', replyTo: '', subject: "${component} - Pipe Line Failed", to: 'praveen.gaju94@gmail.com', mimeType: 'text/html'
        }
    }
}