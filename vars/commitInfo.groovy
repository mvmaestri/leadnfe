def call(commit) {
    return commit != null ? "'${commit.commitId.take(7)}'  *${commit.msg}*  by ${commit.author} \n" : ""
}