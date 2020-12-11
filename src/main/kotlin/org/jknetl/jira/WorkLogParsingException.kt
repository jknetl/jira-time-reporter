package org.jknetl.jira

/**
 * Exception representing work log parsing error
 */
class WorkLogParsingException(message: String, cause: Throwable?) : RuntimeException(message, cause) {
    constructor(message: String) : this(message, null)
}