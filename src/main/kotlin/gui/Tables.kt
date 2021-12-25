package gui

import classes.*

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

    fun teamTable(team: Team) = MutableTable<Competitor>(
        columns = listOf(
            Column("surname", true, { surname }, {}),
            Column("name", true, { name }, {}),
            Column("birth", true, { birth }, {}),
            Column("title", true, { title }, {}),
            Column("medical examination", true, { medicalExamination }, {}),
            Column("medical insurance", true, { medicalInsurance }, {})
        ),
        tableData = team.competitors.toMutableList(),
        delete = { GUI.database.deleteCompetitor(it.id) },
        add = { GUI.database.getCompetitor(GUI.database.createEmptyCompetitor(team)) }
    )

    fun groupTable(group: Group) = Table<CompetitorInCompetition>(
        columns = listOf(
            Column("number", false, { number }, {}),
            Column("team", false, { team.name }, {}),
            Column("surname", false, { surname }, {}),
            Column("name", false, { name }, {}),
            Column("birth", false, { birth }, {}),
            Column("title", false, { title }, {}),
            Column("medical examination", false, { medicalExamination }, {}),
            Column("medical insurance", false, { medicalInsurance }, {})
        ),
        tableData = (GUI.database.getCompetition()?.competitors?.filter { it.group == group }
            ?: listOf<CompetitorInCompetition>()).toMutableList(),
    )

    fun checkpointsTable(group: Group) = MutableTable<CheckPoint>(
        columns = listOf(
            Column("name", true, { name }, { name = it })
        ),
        tableData = group.checkPointNames.map { CheckPoint(it, mapOf()) }.toMutableList(),
        delete = { group.checkPointNames.remove(it.name) },
        add = {
            group.checkPointNames.add("")
            CheckPoint("", mapOf())
        }
    )
}