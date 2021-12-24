import classes.Applications
import classes.Competition
import classes.Group
import classes.IncompleteCompetition
import sortition.Sortition
import standings.StandingsInGroups
import standings.StandingsInTeams

class Model {
    private val viewers = mutableSetOf<ModelViewer>()

    private var competition: Competition? = null
    private var applications: Applications? = null
    private var groups: List<Group>? = null
    private var standingsInTeams: StandingsInTeams? = null
    private var standingsInGroups: StandingsInGroups? = null

    fun addViewer(viewer: ModelViewer) {
        viewers.add(viewer)
    }

    private fun notifyViewers(notificationFunction: (viewer: ModelViewer) -> Unit) {
        viewers.forEach(notificationFunction)
    }

    fun loadGroups(groups: List<Group>) {
        this.groups = groups
        notifyViewers { it.groupsLoaded() }
    }

    fun loadApplications(applications: Applications) {
        this.applications = applications
        notifyViewers { it.applicationsLoaded() }
    }

    fun generateSortition() {
        val newCompetition = Sortition(
            applications ?: throw Exception("Empty applications!"),
            groups ?: throw Exception("Available groups not found!")
        ).generateCompetition()
        competition = newCompetition
        notifyViewers { it.sortitionGenerated(newCompetition) }
    }

    fun loadResults(results: IncompleteCompetition) {
        competition?.setCheckpointsFromIncomplete(results) ?: throw Exception("Competition was not generated yet!")
        notifyViewers { it.resultsLoaded() }
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