package classes

open class CompetitorInCompetition(
    competitor: Competitor,
    val number: String,
    val group: Group,
    val team: Team
) : Competitor(competitor) {
    fun getInfo() = listOf(number, surname, name, birth, title, team.name)
}