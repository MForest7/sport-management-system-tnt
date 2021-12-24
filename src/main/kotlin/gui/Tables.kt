package gui

import classes.Competitor
import classes.Team

data class CompetitorWithTeam(val team: Team, val competitor: Competitor)

object Tables {
    fun applictionsTable() = MutableTable<CompetitorWithTeam>(
        columns = listOf(

        ),
        tableData = mutableListOf(

        ),
        add = {
            GUI.database.createEmptyCompetitor()
        },
        delete = {
            GUI.database.deleteCompetitor(it.competitor.id)
        }
    )
}