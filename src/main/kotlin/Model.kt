import classes.Competition
import classes.IncompleteCheckpoint
import classes.Team
import sortition.generateSortition
//import standings.StandingsInGroups
//import standings.StandingsInTeams
import java.lang.Exception

class Model {
    private val viewers = mutableSetOf<ModelViewer>()

    private var competition: Competition? = null
    private var applications: List<Team>? = null
//    private var standingsInTeams: StandingsInTeams? = null
//    private var standingsInGroups: StandingsInGroups? = null

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
        val newCompetition = generateSortition(applications ?: throw Exception("Empty applications!"))
        this.competition = newCompetition
        notifyViewers { it.sortitionGenerated(newCompetition) }
    }

    fun uploadResults(results: List<IncompleteCheckpoint>) {
        competition?.setCheckpointsFromIncomplete(results) ?: throw Exception("Competition empty!")
        notifyViewers { it.resultsUploaded() }
    }

    fun generateStandingsInTeams() {
//        val newStandings = standings.generateStandingsInTeams(
//            competition ?: throw Exception("Competition empty!")
//        )
//        standingsInTeams = newStandings
        notifyViewers { it.standingsInTeamsGenerated(competition!!) }
    }

    fun generateStandingsInGroups() {
//        val newStandings = standings.generateStandingsInGroups(
//            competition ?: throw Exception("Competition empty!")
//        )
//        standingsInGroups = newStandings
        notifyViewers { it.standingsInGroupsGenerated(competition!!) }
    }
}