package org.jknetl.jira

import org.json.JSONObject
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * Represents a work log in Jira.
 */
data class WorkLog(
    val issue: String,
    val spent: Duration,
    val started: LocalDateTime,
    val comment: String?
) {

    /**
     * Create JSONObject representing this WorkLog in a format which can be consumed by JIRA v2 API
     *
     * @return JSONObject representing this work log
     */
    fun toJsonObject(): JSONObject {
        val node = JSONObject()
        val offsetDateTime = started.atZone(ZoneId.systemDefault()).toOffsetDateTime()
        DateTimeFormatter.ISO_DATE_TIME
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd\'T\'HH:mm:ss.SSSZ")

        node.put("timeSpent", spent.toMinutes().toString() + "m")
        node.put("started", formatter.format(offsetDateTime))
        node.put("comment", comment)

        return node
    }

    fun prettyString(): String {
        val detail =  if (comment == null) "" else " ($comment)"
        val dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
        val timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
        var duration = formatDuration(spent)

        return "${dateTimeFormatter.format(started)} - ${timeFormatter.format(getEnd().toLocalTime())} ($duration): $issue$detail"
    }

    private fun formatDuration(duration: Duration): String {
        var builder = StringBuilder()
        if (duration.toHours() != 0L) builder.append("${duration.toHoursPart()}h")
        if (duration.toMinutesPart() != 0) builder.append(" ${duration.toMinutesPart()}min")
        return builder.trim().toString()
    }

    private fun getEnd(): LocalDateTime {
        return started.plus(spent)
    }
}