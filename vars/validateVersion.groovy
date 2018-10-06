def call() {
    
    currentBuild.description = 'Validating version'

    fileExists('package.json')
    
    String versionPattern = /^[0-9]+\.[0-9]+\.[0-9]+\-?(build)?[0-9]?/
    
    branchVersion = env.BRANCH_NAME.replace('-','')
    branchVersion = branchVersion.replace('release/','')
    branchVersion = branchVersion.replace('hotfix/','')
    branchVersion = branchVersion.replace('patch/','')
    branchVersion = branchVersion.replace('feature/','')
    branchVersion = branchVersion.replace('patch/','')
    version = sh(script: 'cat package.json | python -c "import sys, json; print json.load(sys.stdin)[\'version\']"', returnStdout: true).trim()
    assert version ==~ versionPattern
    if (branchVersion > version) {
        currentBuild.description = "Valid version: ${branchVersion}"
    } else {
        currentBuild.description = "Version (${branchVersion}) from branch name should be bigger than version from package.json (${version})"
    }
    
}