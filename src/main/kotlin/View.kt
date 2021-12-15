import classes.Competition
import classes.Config
import classes.FileManager
import standings.*

interface ModelViewer {
    fun applicationsUploaded()
    fun sortitionGenerated(competition: Competition)
    fun resultsUploaded()
    fun standingsInTeamsGenerated(standingsInTeams: StandingsInTeams)
    fun standingsInGroupsGenerated(standingsInGroups: StandingsInGroups)
}


class ShellViewer(
    private val fileManager: FileManager
) : ModelViewer {
    override fun applicationsUploaded() {}

    override fun sortitionGenerated(competition: Competition) {
        fileManager.sortitionWriter.print(competition)
    }

    override fun resultsUploaded() {}

    override fun standingsInTeamsGenerated(standingsInTeams: StandingsInTeams) {
        fileManager.standingsInTeamsWriter.print(standingsInTeams)
    }

    override fun standingsInGroupsGenerated(standingsInGroups: StandingsInGroups) {
        fileManager.standingsInGroupsWriter.print(standingsInGroups)
    }
}

