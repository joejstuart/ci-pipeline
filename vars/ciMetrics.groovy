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
    def customDataTags = [:]
    // A map to store the data sent to influx
    def customDataMap = ["ci_pipeline": [:]]
    // Global tags
    def customDataMapTags = [:]
    // This will prefix the data sent to influx. Usually set to the job name.
    def prefix = "ci_pipeline"
    // The influx target configured in jenkins
    def influxTarget = "localInflux"
    // The influx database that will be written to
    def measurement = "ci_pipeline"
    def cimetrics = new CIMetrics()

    /**
     * Call this method to record the step run time
     * @param name - the step name
     * @param body - the enclosing step body
     */
    def timed(String name, Closure body) {
		customDataMap[measurement][name] = cimetrics.timed(body)
    }

    /**
     * Write customDataMap to influxDB
     */
    def writeToInflux() {
        cimetrics.writeToInflux(influxTarget, prefix, customDataMap,
                customDataMapTags, customData, customDataTags)
    }
}
