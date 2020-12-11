package org.jknetl.jira

/**
 * Exception when uploading work logs to Jira through rest API
 */
class WorkLogUploadException(message: String, cause: Throwable?) : RuntimeException(message, cause) {

    constructor(message: String) : this(message, null)
}