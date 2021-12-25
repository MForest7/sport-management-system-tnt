import classes.*
import sortition.Sortition
import standings.StandingsInGroups
import standings.StandingsInTeams

class Model {
    private val viewers = mutableSetOf<ModelViewer>()

    private var competition: Competition? = null
    private var applications: Applications? = null
    private var rules: Rules? = null
    private var standingsInTeams: StandingsInTeams? = null
    private var standingsInGroups: StandingsInGroups? = null

    fun addViewer(viewer: ModelViewer) {
        viewers.add(viewer)
    }

    private fun notifyViewers(notificationFunction: (viewer: ModelViewer) -> Unit) {
        viewers.forEach(notificationFunction)
    }

    fun loadGroups(groups: List<Group>) {
        val newRules = Rules(groups)
        this.rules = newRules
        notifyViewers { it.groupsLoaded(newRules) }
    }

    fun loadApplications(applications: Applications) {
        this.applications = applications
        notifyViewers { it.applicationsLoaded(applications) }
    }

    fun generateSortition() {
        val newCompetition = Sortition(
            applications ?: throw Exception("Empty applications!"),
            rules ?: throw Exception("Available groups not found!")
        ).generateCompetition()
        competition = newCompetition
        notifyViewers { it.sortitionGenerated(newCompetition) }
    }

    fun loadResults(results: IncompleteCompetition) {
        competition?.setCheckpointsFromIncomplete(results) ?: throw Exception("Competition was not generated yet!")
        notifyViewers { it.resultsLoaded(results) }
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