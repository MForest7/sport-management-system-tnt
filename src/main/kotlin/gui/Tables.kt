package gui

import classes.Competitor
import classes.Team

data class CompetitorWithTeam(val team: Team, val competitor: Competitor)

object Tables {
    fun applicationsTable() = Table<CompetitorWithTeam>(
        columns = listOf(
            Column("team", false, { team.name }, {}),
            Column("surname", false, { competitor.surname }, {}),
            Column("name", false, { competitor.name }, {}),
            Column("birth", false, { competitor.birth }, {}),
            Column("title", false, { competitor.title }, {}),
            Column("medical examination", false, { competitor.medicalExamination }, {}),
            Column("medical insurance", false, { competitor.medicalInsurance }, {})
        ),
        tableData = GUI.database.getAllApplications().teams.map {
            it.competitors.map { competitor -> CompetitorWithTeam(it, competitor) }
        }.flatten().toMutableList()
    )
}