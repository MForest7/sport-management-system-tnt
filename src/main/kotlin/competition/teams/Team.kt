package competition.teams

import competition.competitors.Competitor

data class Team(
    val name: String,
    val competitors: MutableList<Competitor>
)