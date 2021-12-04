package classes

class Competition(
    val checkpoints: List<CheckPoint>,
    val competitors: List<CompetitorInCompetition>,
    val numberMatching: Map<String, CompetitorInCompetition>
) {
    val start = checkpoints.first()
    val finish = checkpoints.last()
}