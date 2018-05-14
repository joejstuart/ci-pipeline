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
}
