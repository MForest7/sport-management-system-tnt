package gui

import GUIController
import GUIViewer
import classes.*

//data class CompetitorWithTeam(val team: Team, val competitor: Competitor)

data class CheckPointWithCount(val checkPoint: CheckPoint, val index: Int)

object Tables {
    /*fun applicationsTable() = Table<CompetitorWithTeam>(
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
    )*/

    fun teamTable(team: Team, controller: GUIController, viewer: GUIViewer) = MutableTable<Competitor>(
        columns = listOf(
            Column("surname", true, { surname }, { surname = it; controller.updateApplications() }),
            Column("name", true, { name }, { name = it; controller.updateApplications() }),
            Column("birth", true, { birth }, { birth = it; controller.updateApplications() }),
            Column("title", true, { title }, { title = it; controller.updateApplications() }),
            Column(
                "medical examination",
                true,
                { medicalExamination },
                { medicalExamination = it; controller.updateApplications() }),
            Column(
                "medical insurance",
                true,
                { medicalInsurance },
                { medicalInsurance = it; controller.updateApplications() })
        ),
        tableData = team.competitors.toMutableList(),
        delete = {
            team.competitors.remove(it)
            controller.updateApplications()
        },
        add = {
            val competitor = Competitor("", "", "", "", "", "", "")
            team.competitors.add(competitor)
            controller.updateApplications()
            competitor
        }
    )

    fun groupTable(group: Group, controller: GUIController, viewer: GUIViewer) = Table<CompetitorInCompetition>(
        columns = listOf(
            Column("number", false, { number }, {}),
            Column("team", false, { team.name }, {}),
            Column("surname", false, { surname }, {}),
            Column("name", false, { name }, {}),
            Column("birth", false, { birth }, {}),
            Column("title", false, { title }, {}),
            Column("medical examination", false, { medicalExamination }, {}),
            Column("medical insurance", false, { medicalInsurance }, {}),
            Column(
                "time",
                false,
                { viewer.competition?.start?.timeMatching?.get(this)?.first()?.stringRepresentation ?: "" },
                {})
        ),
        tableData = (viewer.competition?.competitors?.filter { it.group == group } ?: listOf()).toMutableList(),
    )

    private fun columnsByCheckpoints(
        group: Group,
        controller: GUIController,
        viewer: GUIViewer
    ): List<Column<CompetitorInCompetition>> {
        val competition = viewer.competition
        require(competition != null) { }
        val checkpoints =
            listOf(competition.start) + group.checkPointNames.mapNotNull { name -> competition.checkpoints.find { it.name == name } }
        val withCount = checkpoints.mapIndexed { index, checkPoint ->
            CheckPointWithCount(checkPoint, checkpoints.take(index).count { it.name == checkPoint.name })
        }
        return withCount.map { checkpoint ->
            Column(
                checkpoint.checkPoint.name, true,
                { checkpoint.checkPoint.timeMatching[this]?.get(checkpoint.index)?.stringRepresentation ?: "" },
                { line ->
                    val matching = checkpoint.checkPoint.timeMatching[this] ?: mutableListOf()
                    matching += MutableList<Time>(maxOf(0, checkpoint.index + 1 - matching.size)) { Time(it) }
                    println(checkpoint.index)
                    println(matching)
                    matching[checkpoint.index] = Time(line)
                    checkpoint.checkPoint.timeMatching[this] = matching
                    controller.updateResults(competition)
                }
            )
        }
    }

    fun checkpointsTable(group: Group, controller: GUIController, viewer: GUIViewer) = Table<CompetitorInCompetition>(
        columns = listOf(Column<CompetitorInCompetition>("name", false, { name }, {}))
                + columnsByCheckpoints(group, controller, viewer),
        tableData = (viewer.competition?.competitors?.filter { it.group == group } ?: listOf()).toMutableList(),
    )

    fun standingsInGroupTable(group: Group, controller: GUIController, viewer: GUIViewer) = Table(
        columns = listOf(
            Column("place", false, { place }, {}),
            Column("number", false, { competitor.number }, {}),
            Column("surname", false, { competitor.surname }, {}),
            Column("name", false, { competitor.name }, {}),
            Column("birth", false, { competitor.birth }, {}),
            Column("title", false, { competitor.title }, {}),
            Column("team", false, { competitor.team.name }, {}),
            Column("time", false, { time }, {}),
            Column("gap", false, { gap }, {}),
        ),
        tableData = (viewer.standingsInGroups?.standings?.flatMap {
            it.records
        }?.filter { it.competitor.group == group } ?: listOf()).toMutableList(),
    )

    fun standingsTeams(controller: GUIController, viewer: GUIViewer) = Table(
        columns = listOf(
            Column("Team name", false, { team.name }, {}),
            Column("Place", false, { place.toString() }, {}),
            Column("Points", false, { points.toString() }, {})
        ),
        tableData = (viewer.standingsInTeams?.standings ?: listOf()).toMutableList()
    )

    fun showConfig(controller: GUIController, viewer: GUIViewer) = Table<Group>(
        columns = listOf(
            Column<Group>(
                "Group name",
                false,
                { name },
                {})
        ) + (0 until (viewer.rules?.groups?.maxOfOrNull { it.checkPointNames.size } ?: 0)).map { index ->
            Column<Group>(
                "Checkpoint",
                false, {
                    if (index < checkPointNames.size) {
                        checkPointNames[index]
                    } else {
                        ""
                    }
                }, {})
        },
        tableData = (viewer.rules?.groups ?: listOf()).toMutableList()
    )
}