package standings

import classes.*
import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import com.github.doyaaaaaken.kotlincsv.client.ICsvFileWriter
import java.io.File
import kotlin.math.max

private fun Competition.timeOf(competitor: CompetitorInCompetition): Time? {
    val timeAtStart = start.timeMatching[competitor]
    val timeAtFinish = finish.timeMatching[competitor]
    return if (timeAtStart != null) timeAtFinish?.minus(timeAtStart) else null
}

data class RecordInStandings(
    val competitor: CompetitorInCompetition,
    val time: String?,
    val place: Int?,
    val gap: String?
) {
    constructor(competitor: CompetitorInCompetition) : this(competitor, null, null, null)
}

private fun Time?.gapFrom(other: Time?): String? {
    if (this == null) return null
    if (other == null) return null
    if (this.time <= other.time) return null
    return (this - other).formatToGap
}

class StandingsOfGroup(val competition: Competition, val group: Group, competitors: List<CompetitorInCompetition>) {
    val records: List<RecordInStandings>
    val timeOfFirst: Time

    init {
        val (finishedCompetitors, notFinishedCompetitors) = competitors.partition { competition.timeOf(it) != null }
        val finalOrder = finishedCompetitors.sortedBy { competition.timeOf(it) }
        timeOfFirst = competition.timeOf(finalOrder.first()) ?: Time(0)

        val places = finalOrder.associateWith { competitor ->
            1 + finalOrder.count { competition.timeOf(competitor).gapFrom(competition.timeOf(it)) != null }
        }

        records = finalOrder.map {
            RecordInStandings(
                it,
                competition.timeOf(it)?.stringRepresentation,
                places[it],
                competition.timeOf(it).gapFrom(timeOfFirst) ?: ""
            )
        }.plus(notFinishedCompetitors.map { RecordInStandings(it) })
    }
}

private fun getFinalSingleRecord(index: Int, record: RecordInStandings): List<Any> {
    fun CompetitorInCompetition.getInfo() = listOf(number, surname, name, birth, title, team.name)
    return listOf<Any>(index + 1) + record.competitor.getInfo() +
            listOf(record.time ?: "снят", record.place ?: "", record.gap ?: "")
}

private fun printStandingsForSingleGroup(writer: ICsvFileWriter, standings: StandingsOfGroup) {
    writer.writeRow(listOf(standings.group.name) + List(10) { "" })
    val topLine =
        listOf("№ п/п", "Номер", "Фамилия", "Имя", "Г.р.", "Разр.", "Команда", "Результат", "Место", "Отставание")
    writer.writeRow(topLine)
    writer.writeRows(standings.records.mapIndexed { index, it -> getFinalSingleRecord(index, it) })
}

fun printStandingsByGroups(file: File, standingsInGroups: List<StandingsOfGroup>) {
    CsvWriter().open(file, append = true) {
        this.writeRow(listOf("Протокол результатов.") + List(9) { "" })
        standingsInGroups.forEach { standings -> printStandingsForSingleGroup(this, standings) }
    }
}

private fun StandingsOfGroup.pointsOf(competitor: CompetitorInCompetition): Double {
    val time = competition.timeOf(competitor)
    return if (time != null)
        max(0.0, 100.0 * (2 - time.time.toDouble() / timeOfFirst.time.toDouble()))
    else 0.0
}

private fun printStandingsForTeams(writer: ICsvFileWriter, resultOfTeams: MutableMap<Team, Double>) {
    val finalOrder = resultOfTeams.keys.sortedByDescending { resultOfTeams[it] }
    val places = finalOrder.associateWith { team ->
        1 + finalOrder.count { resultOfTeams.getOrDefault(it, 0.0) > resultOfTeams.getOrDefault(team, 0.0) }
    }
    writer.writeRows(finalOrder.mapIndexed { index, team ->
        listOf(index + 1, team.name, resultOfTeams[team]?.toInt(), places[team])
    })
}

fun printStandingsByTeams(file: File, standingsInGroups: List<StandingsOfGroup>) {
    val resultOfTeams = mutableMapOf<Team, Double>()
    standingsInGroups.forEach { standings ->
        standings.records.forEach {
            resultOfTeams[it.competitor.team] =
                (resultOfTeams[it.competitor.team] ?: 0.0) + standings.pointsOf(it.competitor)
        }
    }
    CsvWriter().open(file, append = false) {
        writeRow(listOf("Протокол результатов команд.") + List(3) { "" })
        writeRow(listOf("№ п/п", "Команда", "Результат", "Место"))
        printStandingsForTeams(this, resultOfTeams)
    }
}

private fun Competition.getResultsInGroups(): List<StandingsOfGroup> {
    val standingsInGroups = this.competitors.groupBy { it.group }.map {
        StandingsOfGroup(this, it.key, it.value)
    }
    return standingsInGroups
}

class StandingsInGroups {}
class StandingsInTeams {}

fun generateStandingsInGroups(competition: Competition): StandingsInGroups = TODO()
fun generateStandingsInTeams(competition: Competition): StandingsInTeams = TODO()

fun printStandingsInGroups(standings: StandingsInGroups): Unit = TODO()
fun printStandingsInTeams(standings: StandingsInTeams): Unit = TODO()

fun printStandingsInGroupsToDir(path: String, competition: Competition) {
    val standingsInGroups = competition.getResultsInGroups()
    printStandingsByGroups(File(path), standingsInGroups)
}

fun printStandingsInTeamsToDir(path: String, competition: Competition) {
    val standingsInGroups = competition.getResultsInGroups()
    printStandingsByTeams(File(path), standingsInGroups)
}
