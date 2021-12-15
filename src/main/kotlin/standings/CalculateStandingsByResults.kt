package standings

import classes.*
import kotlin.math.max

private fun Competition.timeOf(competitor: CompetitorInCompetition): Time? {
    val timeAtStart = start.timeMatching[competitor]
    val timeAtFinish = finish.timeMatching[competitor]
    return if (timeAtStart != null) timeAtFinish?.minus(timeAtStart) else null
}

data class RecordInStandings(
    val competitor: CompetitorInCompetition,
    val time: String,
    val place: String,
    val gap: String
) {
    constructor(competitor: CompetitorInCompetition) : this(competitor, "", "", "")
}

class StandingsOfGroup(val competition: Competition, val group: Group, competitors: List<CompetitorInCompetition>) {
    val records: List<RecordInStandings>
    val timeOfFirst: Time

    init {
        val (finishedCompetitors, notFinishedCompetitors) = competitors.partition { competition.timeOf(it) != null }
        val finalOrder = finishedCompetitors.sortedBy { competition.timeOf(it) }
        timeOfFirst = competition.timeOf(finalOrder.first()) ?: Time(0)

        val places = finalOrder.associateWith { competitor ->
            1 + finalOrder.count { competition.timeOf(competitor)?.gapFrom(competition.timeOf(it)) != null }
        }

        records = finalOrder.map {
            RecordInStandings(
                competitor = it,
                time = competition.timeOf(it)?.stringRepresentation ?: "",
                place = places[it].toString(),
                gap = competition.timeOf(it)?.gapFrom(timeOfFirst) ?: ""
            )
        }.plus(notFinishedCompetitors.map { RecordInStandings(it) })
    }

    fun pointsOf(competitor: CompetitorInCompetition): Double {
        val time = competition.timeOf(competitor)
        return if (time != null)
            max(0.0, 100.0 * (2 - time.time.toDouble() / timeOfFirst.time.toDouble()))
        else 0.0
    }
}

data class RecordWithTeam(val team: Team, val points: Double, val place: Int)

class StandingsInGroups(val standings: List<StandingsOfGroup>) {
    constructor(competition: Competition) : this(
        competition.competitors.groupBy { it.group }.map { StandingsOfGroup(competition, it.key, it.value) }
    )

    fun calculateStandingsForTeams(): List<RecordWithTeam> {
        val resultOfTeams = mutableMapOf<Team, Double>()
        standings.forEach { groupStandings ->
            groupStandings.records.forEach {
                resultOfTeams[it.competitor.team] =
                    (resultOfTeams[it.competitor.team] ?: 0.0) + groupStandings.pointsOf(it.competitor)
            }
        }
        val finalOrder = resultOfTeams.keys.sortedByDescending { resultOfTeams[it] }
        val places = finalOrder.associateWith { team ->
            1 + finalOrder.count { resultOfTeams.getOrDefault(it, 0.0) > resultOfTeams.getOrDefault(team, 0.0) }
        }
        return finalOrder.map { RecordWithTeam(it, resultOfTeams.getOrDefault(it, 0.0), places.getOrDefault(it, 0)) }
    }
}

class StandingsInTeams(val standings: List<RecordWithTeam>) {
    constructor(competition: Competition) : this(
        StandingsInGroups(competition).calculateStandingsForTeams()
    )
}
