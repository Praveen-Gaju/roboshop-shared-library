def call() {
    if (!env.sonar_extra_opts) {
        env.sonar_extra_opts = ""
    }

    node('workstation') {

        try {

            stage ('Check Out Code'){
                sh 'ls -l'
                cleanWs()
                sh 'ls -l'
                git branch: 'main', url: "https://github.com/Praveen-Gaju/cart"
                sh 'ls -l'
            }

            sh 'env'


            if (env.BRANCH_NAME != "main"){
                stage('Compile/Build') {
                    common.compile()
                }
            }



            stage('Test Cases') {
                common.testcases()
            }

            stage('Code Quality') {
                common.codequality()
            }

        } catch (e) {
            mail bcc: '', body: "<h1>${component} - Pipe Line Failed \n ${BUILD_URL}</h1>", cc: '', from: 'praveen.gaju94@gmail.com', replyTo: '', subject: "${component} - Pipe Line Failed", to: 'praveen.gaju94@gmail.com', mimeType: 'text/html'
        }
    }
}