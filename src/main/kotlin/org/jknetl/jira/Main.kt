package org.jknetl.jira

import java.nio.file.Paths
import kotlin.system.exitProcess


fun main(args: Array<String>) {
    val workLogs = WorkLogParser().parse(Paths.get(args[0]))

    println("Work logs parsed:")
    workLogs.forEach {
        println(it.prettyString())
    }

    println()
    print("Upload to JIRA? [y/n]")
    var answer: String? = null
    while (answer == null) {
        answer = readLine()
    }
    if ("y" != answer.toLowerCase()){
        println("Exiting")
        exitProcess(0)
    }

    val jiraUrl = args[1]

    print("Enter Jira username: ")
    val user = readLine()!!
    print("Enter Jira password or token: ")
    val password: String = (System.console()?.readPassword() ?: readLine()).toString()

    val client = WorkLogClient("https://$jiraUrl/rest/api/2/", user, password)

    client.postWorkLogs(workLogs)
}