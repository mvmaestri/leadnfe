#!/usr/bin/env groovy

/*
  notify slack parsing tests results
*/

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import hudson.tasks.test.AbstractTestResultAction;
import hudson.model.Actionable;

def call(String title, String type, String context) {

  def subject = "<${env.RUN_DISPLAY_URL}|View in Jenkins> | <${env.RUN_CHANGES_DISPLAY_URL}|Changes>"
  def title_link = "${env.RUN_DISPLAY_URL}"
  def branchName = "${env.BRANCH_NAME}"

  def commit = sh(returnStdout: true, script: 'git rev-parse HEAD')
  def author = sh(returnStdout: true, script: "git --no-pager show -s --format='%an'").trim()
  def message = sh(returnStdout: true, script: 'git log -1 --pretty=%B').trim()

  failed = 0

  @NonCPS
  def getTestSummary = { ->
    def testResultAction = currentBuild.rawBuild.getAction(AbstractTestResultAction.class)
    def summary = ""

    if (testResultAction != null) {
        def total = testResultAction.getTotalCount()
        failed = testResultAction.getFailCount()
        def skipped = testResultAction.getSkipCount()
        summary = summary + ("Passed: " + (total - failed - skipped))
        summary = summary + ("\n Failed: " + failed + " ${testResultAction.failureDiffString}")
        summary = summary + ("\n Skipped: " + skipped)
    } else {
        summary = "No tests found"
    }
    return summary
  }

  def testSummaryRaw = getTestSummary()
  def testSummary = "${testSummaryRaw}"

  JSONObject attachment = new JSONObject();
  
  JSONObject branch = new JSONObject();
  branch.put('title', 'Branch');
  branch.put('value', branchName.toString());
  branch.put('short', true);

  JSONObject commitAuthor = new JSONObject();
  commitAuthor.put('title', 'Triggered by');
  commitAuthor.put('value', author.toString());
  commitAuthor.put('short', true);

  JSONObject commitMessage = new JSONObject();
  commitMessage.put('title', 'Commit');
  commitMessage.put('value', message.toString());
  commitMessage.put('short', false);

  JSONObject testResults = new JSONObject();
  testResults.put('title', 'Results')
  testResults.put('value', testSummary.toString())
  testResults.put('short', false)

  JSONObject contextObject = new JSONObject();
  contextObject.put('value', context.toString())
  contextObject.put('short', false)

  switch(type) {
    case "tests":
      attachment.put('author',"Jenkins");
      attachment.put('title', title.toString());
      attachment.put('title_link',title_link.toString());
      // attachment.put('text', subject.toString());
      attachment.put('fallback', title.toString());
      attachment.put('mrkdwn_in', ["fields"])
      attachment.put('fields', [testResults]);
      break
    case "info":
      attachment.put('author',"Jenkins");
      attachment.put('title', title.toString());
      attachment.put('fallback', title.toString());
      attachment.put('mrkdwn_in', ["fields"])
      attachment.put('fields', [contextObject]);
      break
    default:
      attachment.put('author',"Jenkins");
      attachment.put('title', title.toString());
      attachment.put('title_link',title_link.toString());
      attachment.put('text', subject.toString());
      attachment.put('fallback', title.toString());
      attachment.put('mrkdwn_in', ["fields"])
      attachment.put('fields', [testResults]);
      attachment.put('fields', [branch, commitAuthor, commitMessage, testResults]);
      break
  }
  
  JSONArray attachments = new JSONArray();
  attachments.add(attachment);
  println attachments.toString()

  slackSend (message: subject, attachments: attachments.toString())

  if (type == 'tests' && failed > 0) {
    error "Failed"
  }

}