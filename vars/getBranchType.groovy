def call(String branch_name) {
    def dev_pattern = ".*develop"
    def release_pattern = ".*release/.*"
    def feature_pattern = ".*feature/.*"
    def hotfix_pattern = ".*hotfix/.*"
    def patch_pattern = ".*patch/.*"
    def master_pattern = ".*master"
    if (branch_name =~ dev_pattern) {
        return "develop"
    } else if (branch_name =~ release_pattern) {
        return "release"
    } else if (branch_name =~ master_pattern) {
        return "master"
    } else if (branch_name =~ feature_pattern) {
        return "feature"
    } else if (branch_name =~ patch_pattern) {
        return "patch"
    } else if (branch_name =~ hotfix_pattern) {
        return "hotfix"
    } else {
        return null;
    }
}