import org.centos.dsl.pipelineJobTrigger

def triggerJobs = ['fedora-f26': ['gitUrl': 'https://github.com/CentOS-PaaS-SIG/upstream-fedora-pipeline.git',
                                  'branch': 'master'
                                 ]
                  ]

triggerJobs.each { jobName, jobProps ->
    pipelinejob = new pipelineJobTrigger(this, jobName, jobProps['gitUrl'], jobProps['branch'])
    pipelinejob.create()
}
