import classes.Competition
import classes.FileManager
import standings.StandingsInGroups
import standings.StandingsInTeams

interface ModelViewer {
    fun groupsLoaded()
    fun applicationsLoaded()
    fun sortitionGenerated(competition: Competition)
    fun sortitionLoaded(competition: Competition)
    fun resultsLoaded()
    fun standingsInTeamsGenerated(standingsInTeams: StandingsInTeams)
    fun standingsInGroupsGenerated(standingsInGroups: StandingsInGroups)
}

class DataBaseViewer(
    private val database: DB
) : ModelViewer {
    override fun groupsLoaded() {}

    override fun applicationsLoaded() {}

    override fun sortitionGenerated(competition: Competition) {
        database.setCompetition(competition)
    }

    override fun sortitionLoaded(competition: Competition) {}

    override fun resultsLoaded() {}

    override fun standingsInTeamsGenerated(standingsInTeams: StandingsInTeams) {
    }

    override fun standingsInGroupsGenerated(standingsInGroups: StandingsInGroups) {
    }
}


class ShellViewer(
    private val fileManager: FileManager
) : ModelViewer {
    override fun groupsLoaded() {}

    override fun applicationsLoaded() {}

    override fun sortitionGenerated(competition: Competition) {
        fileManager.sortitionWriter.print(competition)
    }

    override fun sortitionLoaded(competition: Competition) {}

    override fun resultsLoaded() {}

    override fun standingsInTeamsGenerated(standingsInTeams: StandingsInTeams) {
        fileManager.standingsInTeamsWriter.print(standingsInTeams)
    }

    override fun standingsInGroupsGenerated(standingsInGroups: StandingsInGroups) {
        fileManager.standingsInGroupsWriter.print(standingsInGroups)
    }
}

