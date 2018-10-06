def call() {
  String msg = ""
  String repoUrl = "${env.RUN_CHANGES_DISPLAY_URL}"
  def lastId = null
  def prevId = prevBuildLastCommitId()
  def changeLogSets = currentBuild.changeSets

  for (int i = 0; i < changeLogSets.size(); i++) {
      def entries = changeLogSets[i].items
      for (int j = 0; j < entries.length; j++) {
          def entry = entries[j]
          lastId = entry.commitId
          msg = msg + "${commitInfo(entry)}"
      }
  }
  if (prevId != null && lastId != null) {
      msg = msg + "\n<${repoUrl}|View changes in Jenkins>\n"
  }
  return msg
}