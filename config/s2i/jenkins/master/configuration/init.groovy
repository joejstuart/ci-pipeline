@Grapes([
    @Grab(group='org.yaml', module='snakeyaml', version='1.17')
])

import org.yaml.snakeyaml.Yaml
import javaposse.jobdsl.dsl.DslScriptLoader
import javaposse.jobdsl.plugin.JenkinsJobManagement
import java.util.logging.Logger
import hudson.security.*
import jenkins.*
import jenkins.model.*
import java.util.logging.Logger
import jenkins.security.s2m.*
import com.redhat.jenkins.plugins.ci.*
import com.redhat.jenkins.plugins.ci.messaging.*

def logger = Logger.getLogger("")
logger.info("Disabling CLI over remoting")
jenkins.CLI.get().setEnabled(false);
logger.info("Enable Slave -> Master Access Control")
Jenkins.instance.injector.getInstance(AdminWhitelistRule.class).setMasterKillSwitch(false);
Jenkins.instance.save()

logger.info("Setup fedora-fedmsg Messaging Provider")
FedMsgMessagingProvider fedmsg = new FedMsgMessagingProvider("fedora-fedmsg", "tcp://hub.fedoraproject.org:9940", "tcp://172.19.4.24:9941", "org.fedoraproject");
GlobalCIConfiguration.get().addMessageProvider(fedmsg)

logger.info("Setup fedora-fedmsg-stage Messaging Provider")
FedMsgMessagingProvider fedmsgStage = new FedMsgMessagingProvider("fedora-fedmsg-stage", "tcp://stg.fedoraproject.org:9940", "tcp://172.19.4.36:9941", "org.fedoraproject");
GlobalCIConfiguration.get().addMessageProvider(fedmsgStage)

logger.info("Setup fedora-fedmsg-devel Messaging Provider")
FedMsgMessagingProvider fedmsgDevel = new FedMsgMessagingProvider("fedora-fedmsg-devel", "tcp://fedmsg-relay.continuous-infra.svc:4001", "tcp://fedmsg-relay.continuous-infra.svc:2003", "org.fedoraproject");
GlobalCIConfiguration.get().addMessageProvider(fedmsgDevel)

logger.info("Setting Time Zone to be EST")
System.setProperty('org.apache.commons.jelly.tags.fmt.timeZone', 'America/New_York')

env = System.getenv()
JENKINS_SETUP_YAML = env['JENKINS_SETUP_YAML'] ?: "${env['JENKINS_HOME']}/setup.yml"
config = new Yaml().load(new File(JENKINS_SETUP_YAML).text)
Logger logger = Logger.getLogger('seed.groovy')
//def strategy = new GlobalMatrixAuthorizationStrategy()
//strategy.add(hudson.model.Item.CREATE,'anonymous')
//Jenkins.instance.setAuthorizationStrategy(strategy)
//Jenkins.instance.save()

Thread.start {
    WORKSPACE_BASE = "${env['JENKINS_HOME']}"
    def workspace = new File("${WORKSPACE_BASE}")
    // workspace.mkdirs()
    // def seedJobDsl = new File("${WORKSPACE_BASE}/seed.groovy")
    def seedJobDsl = config.seed_jobdsl

    def jobManagement = new JenkinsJobManagement(System.out, [:], workspace)
    new DslScriptLoader(jobManagement).runScript(seedJobDsl)
    logger.info('Created first job')
}

