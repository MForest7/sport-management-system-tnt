import classes.Competition
import classes.Team
import parsers.IncompleteCheckpoint
import sortition.generateSortition
import standings.StandingsInGroups
import standings.StandingsInTeams

class Model {
    private val viewers = mutableSetOf<ModelViewer>()

    private var competition: Competition? = null
    private var applications: List<Team>? = null
    private var standingsInTeams: StandingsInTeams? = null
    private var standingsInGroups: StandingsInGroups? = null

    fun addViewer(viewer: ModelViewer) {
        viewers.add(viewer)
    }

    private fun notifyViewers(notificationFunction: (viewer: ModelViewer) -> Unit) {
        viewers.forEach(notificationFunction)
    }

    fun uploadApplications(applications: List<Team>) {
        this.applications = applications
        notifyViewers { it.applicationsUploaded() }
    }

    fun generateSortition() {
        requireNotNull(applications) { "Empty applications!" }
        competition = generateSortition(applications!!)
        notifyViewers { it.sortitionGenerated() }
    }

    fun uploadResults(results: List<IncompleteCheckpoint>) {
        requireNotNull(competition) { "Competition was not generated yet!" }
        competition!!.setCheckpointsFromIncomplete(results)
        notifyViewers { it.resultsUploaded() }
    }

    fun generateStandingsInTeams() {
        requireNotNull(competition) { "Competition was not generated yet!" }
        standingsInTeams = standings.generateStandingsInTeams(competition!!)
        notifyViewers { it.standingsInTeamsGenerated() }
    }

    fun generateStandingsInGroups() {
        requireNotNull(competition) { "Competition was not generated yet!" }
        standingsInGroups = standings.generateStandingsInGroups(competition!!)
        notifyViewers { it.standingsInGroupsGenerated() }
    }
}