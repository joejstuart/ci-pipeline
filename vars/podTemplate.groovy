def call(parameters = [:], Closure body) {

    def containers = parameters.get("containers")
    def openshift_namespace = parameters.get('openshift_namespace', 'continuous-infra')
    def docker_repo_url = parameters.get('docker_repo_url', '172.30.254.79:5000')
    def podName = parameters.get('podName', "generic-${UUID.randomUUID().toString()}")
    def openshift_service_account = parameters.get("openshift_service_account", 'jenkins')

    def containerTemplates = []

    // add default jenkins slave container
    containerTemplates << containerTemplate(name: 'jnlp',
            image: docker_repo_url + '/' + openshift_namespace + '/jenkins-continuous-infra-slave:stable',
            ttyEnabled: false,
            args: '${computer.jnlpmac} ${computer.name}',
            command: '',
            workingDir: '/workDir')

    containers.each { containerName, containerProps ->
        def tag = parameters.get('tag', 'stable')
        imageUrl = docker_repo_url + '/' + openshift_namespace + '/' + containerName + tag
        containerTemplates << containerTemplate(name: containerName,
                image: imageUrl,
                ttyEnabled: false,
                command: '',
                workingDir: '/workDir')
    }

    podTemplate(name: podName,
            label: podName,
            cloud: 'openshift',
            serviceAccount: openshift_service_account,
            idleMinutes: 0,
            namespace: openshift_namespace,
            containers: containerTemplates,
            volumes: [emptyDirVolume(memory: false, mountPath: '/sys/class/net')]
    ) {
        node(podName) {
            body()
        }
    }
}