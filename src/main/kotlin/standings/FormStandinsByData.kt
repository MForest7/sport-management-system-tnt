package standings

import classes.*
import com.github.doyaaaaaken.kotlincsv.client.CsvFileReader
import com.github.doyaaaaaken.kotlincsv.client.CsvFileWriter
import com.github.doyaaaaaken.kotlincsv.client.CsvWriter

private data class RecordInStandings(val competitor: CompetitorInCompetition, val time: Time?) {
    constructor(competitor: CompetitorInCompetition, competition: Competition) : this(
        competitor,
        competition.finish.timeMatching[competitor]
    )
}

private class StandingsOfGroup(val group: Group, records: List<RecordInStandings>) {
    val competitors = records.sortedWith(nullsLast(compareBy { it.time as Time }))
}

private fun FormListOfStandingsByCompetition(competition: Competition): List <StandingsOfGroup> {
    return competition.competitors.groupBy { it.group }.map {
        StandingsOfGroup(it.key, it.value.map { competitor -> RecordInStandings(competitor, competition) }
    ) }
}

private fun getFullStandings(standings: StandingsOfGroup): List<List<Any>> {
    TODO()
}

private fun printStandingsForSingleGroup(writer: CsvFileWriter, standings: StandingsOfGroup) {
    writer.writeRow(listOf(standings.group.name).plus(List(10){""}))
    val topLine = listOf("№ п/п","Номер","Фамилия","Имя","Г.р.","Разр.","Команда","Результат","Место","Отставание")
    writer.writeRow(topLine)

    TODO()
}

private fun printStandingsByGroups() {
    TODO()
}

private fun printStandingsByTeams() {
    TODO()
}