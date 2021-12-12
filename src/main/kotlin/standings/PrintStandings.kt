package standings

import classes.Competition
import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import com.github.doyaaaaaken.kotlincsv.client.ICsvFileWriter
import java.io.File

fun printStandingsInGroups(standings: StandingsInGroups): Unit = TODO()
fun printStandingsInTeams(standings: StandingsInTeams): Unit = TODO()

private fun printStandingsForSingleGroup(writer: ICsvFileWriter, standings: StandingsOfGroup) {
    writer.writeRow(listOf(standings.group.name) + List(10) { "" })
    val topLine = listOf("№ п/п", "Номер", "Фамилия", "Имя", "Г.р.", "Разр.", "Команда", "Результат", "Место", "Отставание")
    writer.writeRow(topLine)
    writer.writeRows(standings.records.mapIndexed { index, it ->
        listOf(index + 1) + it.competitor.getInfo() + listOf(it.time ?: "снят", it.place ?: "", it.gap ?: "")
    })
}

fun printStandingsInGroupsToFile(standings: StandingsInGroups, path: String) {
    CsvWriter().open(File(path), append = true) {
        this.writeRow(listOf("Протокол результатов.") + List(9) { "" })
        standings.standings.forEach { standings -> printStandingsForSingleGroup(this, standings) }
    }
}

fun printStandingsInTeamsToFile(standings: StandingsInTeams, path: String) {
    CsvWriter().open(File(path), append = false) {
        writeRow(listOf("Протокол результатов команд.") + List(3) { "" })
        writeRow(listOf("№ п/п", "Команда", "Результат", "Место"))
        standings.standings.forEachIndexed { index, it ->
            writeRow(listOf(index + 1, it.team.name, it.points.toInt(), it.place))
        }
    }
}