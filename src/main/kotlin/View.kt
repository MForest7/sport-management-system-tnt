import classes.Competition
import classes.Config
import sortition.printSortition
import standings.*

interface ModelViewer {
    fun applicationsUploaded()
    fun sortitionGenerated(competition: Competition)
    fun resultsUploaded()
    fun standingsInTeamsGenerated(standingsInTeams: StandingsInTeams)
    fun standingsInGroupsGenerated(standingsInGroups: StandingsInGroups)
}


class ShellViewer(private val config: Config) : ModelViewer {
    override fun applicationsUploaded() {}

    override fun sortitionGenerated(competition: Competition) {
        printSortition(config.sortitionFolder, competition)
    }

    override fun resultsUploaded() {}

    override fun standingsInTeamsGenerated(standingsInTeams: StandingsInTeams) {
        printStandingsInTeamsToFile(standingsInTeams, config.resultsInTeams)
    }

    override fun standingsInGroupsGenerated(standingsInGroups: StandingsInGroups) {
        printStandingsInGroupsToFile(standingsInGroups, config.resultsInGroups)
    }
}

