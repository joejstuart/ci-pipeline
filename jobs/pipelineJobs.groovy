import org.centos.dsl.pipelineJobTrigger

def pipelineJobs = ['fedora-f26': ['gitUrl': 'git@github.com:CentOS-PaaS-SIG/ci-pipeline.git',
                                   'branch': 'master'
                                  ]
                   ]

pipelineJobs.each { jobName, jobProps ->
    pipelinejob = new pipelineJobTrigger(this, jobName, jobProps['gitUrl'])
    pipelinejob.create()
}
