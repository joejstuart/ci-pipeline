import org.centos.dsl.pipelineJobTrigger

def triggerJobs = ['fedora-f26': ['gitUrl': 'git@github.com:CentOS-PaaS-SIG/ci-pipeline.git',
                                   'branch': 'master'
                                  ]
                   ]

triggerJobs.each { jobName, jobProps ->
    pipelinejob = new pipelineJobTrigger(this, jobName, jobProps['gitUrl'], jobProps['branch'])
    pipelinejob.create()
}
