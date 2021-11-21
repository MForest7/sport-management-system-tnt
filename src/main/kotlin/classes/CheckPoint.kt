package classes

data class CheckPoint(
    val name: String,
    val timeMatching: Map<CompetitorInCompetition, Time>
)