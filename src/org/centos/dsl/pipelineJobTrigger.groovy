package org.centos.dsl


class pipelineJobTrigger {

    def instance
    def name
    def gitUrl
    def gitBranch

    pipelineJobTrigger(instance, name, gitUrl, gitBranch) {
        this.instance = instance
        this.name = name
        this.gitUrl = gitUrl
        this.gitBranch = gitBranch
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
               stringParam(name: 'ghprbPullAuthorLogin', '', 'Pull Request Author username')
               stringParam(name: 'SLAVE_TAG', 'stable', 'Tag for slave image')
               stringParam(name: 'RPMBUILD_TAG', 'stable', 'Tag for rpmbuild image')
               stringParam(name: 'CLOUD_IMAGE_COMPOSE_TAG', 'stable', 'Tag for cloud-image-compose image')
               stringParam(name: 'SINGLEHOST_TEST_TAG', 'stable', 'Tag for singlehost test image')
               stringParam(name: 'DOCKER_REPO_URL', '172.30.254.79:5000', 'Docker repo url for Openshift instance')
               stringParam(name: 'OPENSHIFT_NAMESPACE', 'continuous-infra', 'Project namespace for Openshift operations')
               stringParam(name: 'OPENSHIFT_SERVICE_ACCOUNT', 'jenkins', 'Service Account for Openshift operations')
               stringParam(name: 'CI_MESSAGE', CANNED_CI_MESSAGE, 'CI_MESSAGE')
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
               }
           }
       }

       instance.queue(name)

    }

/*
    properties(
            [
                    buildDiscarder(logRotator(artifactDaysToKeepStr: '30', artifactNumToKeepStr: '100', daysToKeepStr: '90', numToKeepStr: '100')),
                    [$class: 'JobPropertyImpl', throttle: [count: 150, durationName: 'hour', userBoost: false]],
                    parameters(
                            [
                            ]
                    ),
            ]
    )
*/
}
