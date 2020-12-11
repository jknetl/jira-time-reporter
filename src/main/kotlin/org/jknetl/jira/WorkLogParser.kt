package org.jknetl.jira

import org.yaml.snakeyaml.Yaml
import java.nio.file.Files
import java.nio.file.Path
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * Parses work logs from a file.
 */
class WorkLogParser {

    val workingHoursStart = LocalTime.of(8, 0)

    fun parse(path: Path): List<WorkLog> {
        val yaml = Yaml()
        val workLogs = mutableListOf<WorkLog>()

        Files.newBufferedReader(path).use { reader ->
            val yamlFile: Map<String, List<Any>> = yaml.load(reader)
            val listOfDays = yamlFile["workLogs"]

            addWorkLogs(listOfDays, workLogs)
        }
        return workLogs
    }

    private fun addWorkLogs(
        days: List<Any>?,
        workLogs: MutableList<WorkLog>
    ): Unit? {
        var startTime: LocalTime
        return days?.forEach {
            val map = it as Map<String, List<Any>>
            val day = map.keys.stream().findFirst().orElseThrow { WorkLogParsingException("Missing day definition") }
            val records = map[day]
            startTime = workingHoursStart

            records?.forEach { issues ->
                val mapOfRecords = issues as Map<String, Any>
                mapOfRecords.forEach { issue, v ->
                    if (v is Map<*, *>) {
                        //TODO: implement this
                        TODO("Not implemented yet")
                    } else {
                        val duration = parseDuration(v as String)
                        val started: LocalDateTime = LocalDateTime.of(parseDate(day), startTime)
                        startTime = startTime.plus(duration)
                        workLogs.add(WorkLog(issue, duration, started, null))
                    }
                }
            }
        }
    }

    private fun parseDate(day: String): LocalDate {
        val parser = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val year = Year.now()
        try {
            return LocalDate.parse("${day}/$year", parser)
        } catch (e: DateTimeParseException) {
            throw (WorkLogParsingException("Cannot parse date $day", e))
        }
    }

    private fun parseDuration(value: String): Duration {
        try {
            return Duration.parse("PT" + value.toUpperCase())
        } catch (e: DateTimeParseException) {
            throw WorkLogParsingException("Cannot parse workLog 'timeSpent': $value")
        }
    }
}