import org.centos.dsl.pipelineJob
import org.centos.dsl.pipelineJobTrigger

def pipelineJobs = ['fedora-f26': ['gitUrl': 'git@github.com:CentOS-PaaS-SIG/ci-pipeline.git',
                                   'branch': 'master'
                                  ]
                   ]

pipelineJobs.each { jobName ->
    pipelinejob = new pipelineJob(this, jobName, jobName['gitUrl'])
    pipelinejob.create()
}
