package classes

data class CheckPoint(
    val name: String,
    val timeMatching: MutableMap<CompetitorInCompetition, Time>
)