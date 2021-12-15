import classes.Applications
import classes.Competition
import classes.IncompleteCompetition
import classes.Team
import sortition.Sortition
import standings.StandingsInGroups
import standings.StandingsInTeams
import java.lang.Exception

class Model {
    private val viewers = mutableSetOf<ModelViewer>()

    private var competition: Competition? = null
    private var applications: Applications? = null
    private var standingsInTeams: StandingsInTeams? = null
    private var standingsInGroups: StandingsInGroups? = null

    fun addViewer(viewer: ModelViewer) {
        viewers.add(viewer)
    }

    private fun notifyViewers(notificationFunction: (viewer: ModelViewer) -> Unit) {
        viewers.forEach(notificationFunction)
    }

    fun uploadApplications(applications: Applications) {
        this.applications = applications
        notifyViewers { it.applicationsUploaded() }
    }

    fun generateSortition() {
        val newCompetition = Sortition(applications ?: throw Exception("Empty applications!")).generateCompetition()
        competition = newCompetition
        notifyViewers { it.sortitionGenerated(newCompetition) }
    }

    fun uploadResults(results: IncompleteCompetition) {
        competition?.setCheckpointsFromIncomplete(results) ?: throw Exception("Competition was not generated yet!")
        notifyViewers { it.resultsUploaded() }
    }

    fun generateStandingsInTeams() {
        val newStandings = StandingsInTeams(
            competition ?: throw Exception("Competition was not generated yet!")
        )
        standingsInTeams = newStandings
        notifyViewers { it.standingsInTeamsGenerated(newStandings) }
    }

    fun generateStandingsInGroups() {
        val newStandings = StandingsInGroups(
            competition ?: throw Exception("Competition was not generated yet!")
        )
        standingsInGroups = newStandings
        notifyViewers { it.standingsInGroupsGenerated(newStandings) }
    }
}