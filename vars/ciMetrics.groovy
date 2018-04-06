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


    def cimetrics = new CIMetrics()

    def jobMeasurement = cimetrics.defaultMeasurement()

    // fields to store in the jenkins_custom_data measurement
    def customData = [:]
    // tags to store in the jenkins_custom_data measurement
    def customDataTags = [jobMeasurement: [:]]
    // A map to store the data sent to influx
    def customDataMap = [jobMeasurement: [:]]
    // Global tags
    def customDataMapTags = [:]
    // This will prefix the data sent to influx. Usually set to the job name.
    def prefix = jobMeasurement
    // The influx target configured in jenkins
    def influxTarget = "localInflux"


    /**
     * Call this method to record the step run time
     * @param name - the step name
     * @param body - the enclosing step body
     */
    def timed(String name, Closure body) {
        customDataMap[jobMeasurement][name] = cimetrics.timed(body)
    }

    /**
     *
     * @param key
     * @param value
     * @param measurement
     * @return
     */
    def setMetricField(String measurement, String key, def value) {
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
    def setMetricTag(String measurement, String key, String value) {
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
