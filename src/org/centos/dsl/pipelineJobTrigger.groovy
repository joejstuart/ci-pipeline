package org.centos.dsl


class pipelineJobTrigger {

    def instance
    def name
    def gitUrl
    def gitBranch
    def script

    pipelineJobTrigger(instance, name, gitUrl, gitBranch, script) {
        this.instance = instance
        this.name = name
        this.gitUrl = gitUrl
        this.gitBranch = gitBranch
        this.script = script
    }

    def create() {
       instance.pipelineJob(name) {
           description('Job created from job dsl')
           parameters {
               stringParam('PROVIDED_KOJI_TASKID', '', 'Give an integer only task id to use those artifacts and bypass the rpm build stage')
               stringParam('ghprbActualCommit', 'master', 'The GitHub pull request commit')
               stringParam('ghprbGhRepository', '', 'The repo the PR is against')
               stringParam('sha1', '', '')
               stringParam('ghprbPullId', '', 'Pull Request Number')
               stringParam('ghprbPullAuthorLogin', '', 'Pull Request Author username')
               stringParam('SLAVE_TAG', 'stable', 'Tag for slave image')
               stringParam('RPMBUILD_TAG', 'stable', 'Tag for rpmbuild image')
               stringParam('CLOUD_IMAGE_COMPOSE_TAG', 'stable', 'Tag for cloud-image-compose image')
               stringParam('SINGLEHOST_TEST_TAG', 'stable', 'Tag for singlehost test image')
               stringParam('DOCKER_REPO_URL', '172.30.254.79:5000', 'Docker repo url for Openshift instance')
               stringParam('OPENSHIFT_NAMESPACE', 'continuous-infra', 'Project namespace for Openshift operations')
               stringParam('OPENSHIFT_SERVICE_ACCOUNT', 'jenkins', 'Service Account for Openshift operations')
               stringParam('CI_MESSAGE', '{}', 'CI_MESSAGE')
           }
           definition {
               cpsScm {
                   scm {
                       git {
                           branch(gitBranch)
                           remote {
                               url(gitUrl)
                           }
                       }

                   }
                   scriptPath(script)
               }
           }
       }

       instance.queue(name)

    }

}
