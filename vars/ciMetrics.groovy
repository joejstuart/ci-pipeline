import org.centos.pipeline.CIMetrics

/*
A class to store build metrics over the lifetime of the build.
Metrics are stored in customDataMap and then sent to influx at
the end of the job. Example usage:

try {
    def stepName = "mystep"
    ciMetrics.timed stepName, {
        stage(stepName) {
            echo "in mystep"
        }
    }
    currentBuild.result = "SUCCESS"
} catch(err) {
    currentBuild.result = "FAILED"
    throw err
} finally {
    ciMetrics.writeToInflux()
}
 */
class ciMetrics {

    // fields to store in the jenkins_custom_data measurement
    def customData = [:]
    // tags to store in the jenkins_custom_data measurement
    def customDataTags = ["ci_pipeline": [:]]
    // A map to store the data sent to influx
    def customDataMap = ["ci_pipeline": [:]]
    // Global tags
    def customDataMapTags = [:]
    // This will prefix the data sent to influx. Usually set to the job name.
    def prefix = "ci_pipeline"
    // The influx target configured in jenkins
    def influxTarget = "localInflux"

    def defaultMeasurement = env.JOB_NAME

    def cimetrics = new CIMetrics()

    def timedSteps = [:]

    /**
     * Call this method to record the step run time
     * @param name - the step name
     * @param body - the enclosing step body
     */
    def timed(String name, Closure body) {
        setCustomDataMap(name, cimetrics.timed(body))
    }

    /**
     *
     * @param key
     * @param value
     * @param measurement
     * @return
     */
    def setCustomDataMap(String key, def value, String measurement=null) {
        if (measurement == null) {
            measurement = defaultMeasurement
        }

        if (!customDataMap[measurement]) {
            customDataMap[measurement] = [:]
        }

        customDataMap[measurement][key] = value
    }

    /**
     *
     * @param key
     * @param value
     * @param measurement
     * @return
     */
    def setCustomDataMapTags(String key, String value, String measurement=null) {
        if (measurement == null) {
            measurement = defaultMeasurement
        }

        if (!customDataMapTags[measurement]) {
            customDataMapTags[measurement] = [:]
        }

        customDataMapTags[measurement][key] = value
    }

    /**
     * Write customDataMap to influxDB
     */
    def writeToInflux() {
        cimetrics.writeToInflux(influxTarget, prefix, customDataMap,
                customDataMapTags, customData, customDataTags)
    }
    
}
