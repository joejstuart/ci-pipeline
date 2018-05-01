import org.centos.pipeline.PipelineUtils
import org.centos.Utils

/**
 * A class of methods used in the Jenkinsfile pipeline.
 * These methods are wrappers around methods in the ci-pipeline library.
 */
class pipelineUtils implements Serializable {

    def pipelineUtils = new PipelineUtils()
    def utils = new Utils()

    /**
     * Method to setup and configure the host the way ci-pipeline requires
     *
     * @param stage Current stage
     * @param sshKey Name of ssh key to use
     * @return
     */
    def setupStage(String stage, String sshKey) {
        pipelineUtils.setupStage(stage, sshKey)
    }

    /**
     * Method to to check last image modified time
     * @param stage Current stage
     * @return
     */
    def checkLastImage(String stage) {
        pipelineUtils.checkLastImage(stage)
    }

    /**
     * Method to to find RSYNC_BRANCH to use on artifacts server
     * @return
     */
    def getRsyncBranch() {
        pipelineUtils.getRsyncBranch()
    }

    /**
     * Method to set message fields to be published
     * @param messageType ${MAIN_TOPIC}.ci.pipeline.<defined-in-README>
     * @return
     */
    def setMessageFields(String messageType) {
        pipelineUtils.setMessageFields(messageType)
    }

    /**
     * Method to send message
     * @param msgTopic The topic to send the message on
     * @param msgProps The message properties in key=value form, one key/value per line ending in '\n'
     * @param msgContent Message content.
     * @return
     */
    def sendMessage(String msgTopic, String msgProps, String msgContent) {
        pipelineUtils.sendMessage(msgTopic, msgProps, msgContent)
    }

    /**
     * Method to send message and store an audit
     * @param msgTopic The topic to send the message on
     * @param msgProps The message properties in key=value form, one key/value per line ending in '\n'
     * @param msgContent Message content.
     * @param msgAuditFile - File containing all past messages. It will get appended to.
     * @param fedmsgRetryCount number of times to keep trying.
     * @return
     */
    def sendMessageWithAudit(String msgTopic, String msgProps, String msgContent, String msgAuditFile, fedmsgRetryCount) {
        pipelineUtils.sendMessageWithAudit(msgTopic, msgProps, msgContent, msgAuditFile, fedmsgRetryCount)
    }

    /**
     * Method to parse message and inject its key/value pairs as env variables.
     * @param message message from dist-git to parse
     * @return
     */
    def injectFedmsgVars(String message) {
        pipelineUtils.injectFedmsgVars(message)
    }

    /**
     * Library to parse Pagure PR CI_MESSAGE and inject
     * its key/value pairs as env variables.
     * @param prefix - String to prefix env variables with
     * @param message - The CI_MESSAGE
     */
    def injectPRVars(String prefix, String message) {
        pipelineUtils.injectPRVars(prefix, message)
    }

    /**
     * Library to parse Pagure PR CI_MESSAGE and check if
     * it is for a new commit added, the comment contains
     * some keyword, or if the PR was rebased
     * @param message - The CI_MESSAGE
     * @param keyword - The keyword we care about
     */
    def checkUpdatedPR(String message, String keyword) {
        pipelineUtils.checkUpdatedPR(message, keyword)
    }

    /**
     * Method to set default environmental variables. Performed once at start of Jenkinsfile
     * @param envMap Key/value pairs which will be set as environmental variables.
     * @return
     */
    def setDefaultEnvVars(Map envMap = null) {
        pipelineUtils.setDefaultEnvVars(envMap)
    }

    /**
     * Method to set stage specific environmental variables.
     * @param stage Current stage
     * @return
     */
    def setStageEnvVars(String stage) {
        pipelineUtils.setStageEnvVars(stage)
    }

    /**
     * Method to create a text string which is written to the file 'task.env' in the {env.ORIGIN_WORKSPACE} and call
     * runTaskAndReturnLogs()
     * @param stage Current stage
     * @return
     */
    def rsyncData(String stage) {
        pipelineUtils.rsyncData(stage)
    }

    /**
     * Method to provision resources used in the stage
     * @param stage Current stage
     * @return
     */
    def provisionResources(String stage) {
        pipelineUtils.provisionResources(stage)
    }

    /**
     * Method to teardown resources used in the stage
     * @param stage Current stage
     * @return
     */
    def teardownResources(String stage) {
        pipelineUtils.teardownResources(stage)
    }

    /**
     *
     * @param openshiftProject name of openshift namespace/project.
     * @param nodeName podName we are going to get container logs from.
     * @return
     */
    def getContainerLogsFromPod(String openshiftProject, String nodeName) {
        pipelineUtils.getContainerLogsFromPod(openshiftProject, nodeName)
    }

    def verifyPod(openshiftProject, nodeName) {
        pipelineUtils.verifyPod(openshiftProject, nodeName)
    }

    def prepareCredentials() {
        pipelineUtils.prepareCredentials()
    }

    def executeInContainer(stageName, containerName, script) {
        pipelineUtils.executeInContainer(stageName, containerName, script)
    }

    /**
     * Method to prepend 'env.' to the keys in source file and write them in a format of env.key=value in the destination file.
     * @param sourceFile The file to read from
     * @param destinationFile The file to write to
     * @return
     */
    def convertProps(String sourceFile, String destinationFile) {
        utils.convertProps(sourceFile, destinationFile)
    }

    /**
     * Send comment to GH about image operations.
     * @param imageOperationsList list of image operation messages
     * @return
     */
    def sendPRCommentforTags(imageOperationsList) {
        pipelineUtils.sendPRCommentforTags(imageOperationsList)
    }

    /**
     * info about tags to be used
     * @param map
     */
    def printLabelMap(map) {
        pipelineUtils.printLabelMap(map)
    }

    /**
     * Setup container templates in openshift
     * @param openshiftProject Openshift Project
     * @return
     */
    def setupContainerTemplates(String openshiftProject) {
        return pipelineUtils.setupContainerTemplates(openshiftProject)
    }

    /**
     * Build image in openshift
     * @param openshiftProject Openshift Project
     * @param buildConfig
     * @return
     */
    def buildImage(String openshiftProject, String buildConfig) {
        return pipelineUtils.buildImage(openshiftProject, buildConfig)
    }

    /**
     * Build stable image in openshift
     * @param openshiftProject Openshift Project
     * @param buildConfig
     * @return
     */
    def buildStableImage(String openshiftProject, String buildConfig) {
        return pipelineUtils.buildStableImage(openshiftProject, buildConfig)
    }

    /**
     * Using the currentBuild, get a string representation
     * of the changelog.
     * @return String of changelog
     */
    @NonCPS
    def getChangeLogFromCurrentBuild() {
        pipelineUtils.getChangeLogFromCurrentBuild()
    }

    /**
     * Sets the Build displayName and Description based on whether it
     * is a PR or a prod run.
     */
    def setBuildDisplayAndDescription() {
        pipelineUtils.setBuildDisplayAndDescription()
    }

    /**
     * Update the Build displayName and Description based on whether it
     * is a PR or a prod run.
     * Used at start of pipeline to decorate the build with info
     */
    def updateBuildDisplayAndDescription() {
        pipelineUtils.updateBuildDisplayAndDescription()
    }

    /**
     * Sets the Build displayName and Description based on params
     * @param buildName
     * @param buildDesc
     */
    def setCustomBuildNameAndDescription(String buildName, String buildDesc) {
        pipelineUtils.setCustomBuildNameAndDescription(buildName, buildDesc)
    }

/**
 * Check data grepper for presence of a message
 * @param messageID message ID to track.
 * @param retryCount number of times to keep trying.
 * @return
 */
    def trackMessage(String messageID, int retryCount) {
        pipelineUtils.trackMessage(messageID, retryCount)
    }

    /**
     * Initialize message audit file
     * @param auditFile audit file for messages
     * @return
     */
    def initializeAuditFile(String auditFile) {
        pipelineUtils.initializeAuditFile(auditFile)
    }

/**
 * Watch for messages
 * @param msg_provider jms-messaging message provider
 * @param message trigger message
 */
    def watchForMessages(String msg_provider, String message) {
        pipelineUtils.watchForMessages(msg_provider, message)
    }

/**
 *
 * @param nick nickname to connect to IRC with
 * @param channel channel to connect to
 * @param message message to send
 * @param ircServer optional IRC server defaults to irc.freenode.net:6697
 * @return
 */
    def sendIRCNotification(String nick, String channel, String message, String ircServer="irc.freenode.net:6697") {
        pipelineUtils.sendIRCNotification(nick, channel, message, ircServer)
    }

    /**
     * Test if $tag tests exist for $mypackage on $mybranch in fedora dist-git
     * For mybranch, use fXX or master
     * @param mypackage
     * @param mybranch
     * @param tag
     * @return
     */
    def checkTests(String mypackage, String mybranch, String tag) {
        return pipelineUtils.checkTests(mypackage, mybranch, tag)
    }

    /**
     * Test to check if CI_MESSAGE is for a user's fork
     * @param message - The CI_MESSAGE
     * @return boolean
     */
    def checkIfFork(String message) {
        return pipelineUtils.checkIfFork(message)
    }

    /**
     * Mark stage stageName as skipped
     * @param stageName
     * @return
     */
    def skip(String stageName) {
        return pipelineUtils.skip(stageName)
    }

    /**
     * Reads package test.log and return a map of test_name -> test_result
     * @param fileLocation
     * @return
     */
    def parseTestLog(String fileLocation) {
        return pipelineUtils.parseTestLog(fileLocation)
    }

    /**
     * Check the package test results
     * @param testResults
     * @return return the build status
     */
    def checkTestResults(Map testResults) {
        return pipelineUtils.checkTestResults(testResults)
    }

    /**
     * General function to check existence of a file
     * @param fileLocation
     * @return boolean
     */
    def fileExists(String fileLocation) {
        return pipelineUtils.fileExists(fileLocation)
    }

    /**
     * Traverse a CI_MESSAGE with nested keys.
     * @param prefix
     * @param ciMessage
     * @return env map with all keys at top level
     */
    def flattenJSON(String prefix, String message) {
        pipelineUtils.flattenJSON(prefix, message)
    }

    /**
     * Set branch and $prefix_branch based on the candidate branch
     * This is meant to be run with a CI_MESSAGE from a build task
     * You should call flattenJSON on the CI_MESSAGE before using
     * this function
     * @param tag - The tag from the request field e.g. f27-candidate
     * @param prefix - The prefix to add to the keys e.g. fed
     * @return
     */
    def setBuildBranch(String tag, String prefix) {
        pipelineUtils.setBuildBranch(tag, prefix)
    }

    /**
     * @param request - the url that refers to the package
     * @param prefix - env prefix
     * @return
     */
    def repoFromRequest(String request, String prefix) {
        pipelineUtils.repoFromRequest(request, prefix)
    }

    /**
     *
     * @param repo - the package name. (e.g. fed_repo, contra_repo)
     * @param prNum - the PR number
     * @return
     */
    def checkoutGitPR(String repo, String prNum) {
        pipelineUtils.checkoutGitPR(repo, prNum)
    }
/**
 *
 * @param repo - the package name. (e.g. fed_repo, contra_repo)
 * @param branch - the branch to checkout. (e.g. fed_branch, contra_branch)
 * @param rev - the nvr
 * @return
 */
    def checkoutGitRev(String repo, String branch, String rev) {
        pipelineUtils.checkoutGitRev(repo, branch, rev)
    }

}
