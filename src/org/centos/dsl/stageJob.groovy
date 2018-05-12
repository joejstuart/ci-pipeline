package org.centos.dsl


class stageJob {

    def instance
    def name
    def gitUrl
    def gitBranch = 'master'

    def createJob() {
       instance.pipelineJob(name) {
           description('Job created from job dsl')
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

    }
}
