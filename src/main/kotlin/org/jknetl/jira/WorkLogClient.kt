package org.jknetl.jira

import com.mashape.unirest.http.Unirest
import com.mashape.unirest.http.exceptions.UnirestException
import org.apache.http.HttpStatus

/**
 * Handles communication of work logs with JIRA server
 */
class WorkLogClient(
    private val jiraApiUrl : String,
    private val user: String,
    private val password: String
) {


    fun postWorkLogs(workLogs: List<WorkLog>) {
        workLogs.forEach {
            val workLogResourceUrl = jiraApiUrl + "issue/${it.issue}/worklog"
            try {
                val response = Unirest.post(workLogResourceUrl)
                    .basicAuth(user, password)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .queryString("notifyUsers", "false")
                    .body(it.toJsonObject())
                    .asJson()
                /* TODO: this throws JSONNodeException if there is another error (e.g. 401 Unauthorized) as JIRA doesn't
                *   response with json but rather HTML in this case */

                if ( response.status != HttpStatus.SC_CREATED) {
                    throw WorkLogUploadException("Cannot post worklog '${it.prettyString()}' to endpoint '$workLogResourceUrl'")
                }
            } catch (e: UnirestException) {
                throw WorkLogUploadException("Cannot post worklog '${it.prettyString()}' to endpoint '$workLogResourceUrl'", e)
            }
        }
    }
}