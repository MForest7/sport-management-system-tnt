package classes

class Competition(
    val checkpoints: MutableList<CheckPoint>,
    val competitors: List<CompetitorInCompetition>,
    val numberMatching: Map<String, CompetitorInCompetition>
) {
    val start: CheckPoint
        get() {
            return checkpoints.first()
        }
    val finish = checkpoints.last()
}