import classes.Competition
import classes.Config
import sortition.printSortition
import standings.*

interface ModelViewer {
    fun applicationsUploaded()
    fun sortitionGenerated(competition: Competition)
    fun resultsUploaded()
    fun standingsInTeamsGenerated(competition: Competition)
    fun standingsInGroupsGenerated(competition: Competition)
}


class ShellViewer(private val config: Config) : ModelViewer {
    override fun applicationsUploaded() {}

    override fun sortitionGenerated(competition: Competition) {
        printSortition(config.sortitionFolder, competition)
    }

    override fun resultsUploaded() {}

    override fun standingsInTeamsGenerated(competition: Competition) {
        printStandingsInTeamsToDir(competition, config.resultsInTeams)
    }

    override fun standingsInGroupsGenerated(competition: Competition) {
        printStandingsInGroupsToDir(competition, config.resultsInGroups)
    }
}

