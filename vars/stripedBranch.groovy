def call() {
    branchVersion = env.BRANCH_NAME.replace('-','')
    branchVersion = branchVersion.replace('release/','')
    branchVersion = branchVersion.replace('hotfix/','')
    branchVersion = branchVersion.replace('patch/','')
    branchVersion = branchVersion.replace('feature/','')
    branchVersion = branchVersion.replace('patch/','')
    env.STRIPED_BRANCH = branchVersion
    return branchVersion   
}