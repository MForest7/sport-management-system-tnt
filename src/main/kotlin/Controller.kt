import classes.*
import parsers.*


abstract class Controller(protected val model: Model) {
    fun generateSortition() {
        model.generateSortition()
    }

    fun generateStandingsInGroups() {
        model.generateStandingsInGroups()
    }

    fun generateStandingsInTeams() {
        model.generateStandingsInTeams()
    }

    abstract fun uploadApplications()

    abstract fun loadResults()
}


class GUIController(private val model: Model) {
    private var rules: Rules? = null
    var applications: Applications = Applications(listOf())
    var results: IncompleteCompetition? = null

    fun uploadGroups(path: String) {
        val config = JsonReader(path).read()
        val listOfGroups = config.groups.map { (name, checkpointNames) -> Group(name, checkpointNames) }
        rules = Rules(listOfGroups)
        model.loadGroups(listOfGroups)
    }

    fun uploadApplications(folder: String) {
        val listOfTeams = ApplicationsReader(folder).read()
        applications = listOfTeams
        model.loadApplications(listOfTeams)
    }

    fun uploadSortition(folder: String) {
        val finalRules = rules ?: return
        val competition = SortitionReader(folder, finalRules, applications).read()
        model.uploadSortition(competition)
    }

    fun generateSortition() {
        model.generateSortition()
    }

    fun generateStandingsInGroups() {
        model.generateStandingsInGroups()
    }

    fun generateStandingsInTeams() {
        model.generateStandingsInTeams()
    }

    fun uploadResults(folder: String) {
        val checkpointNames = rules?.checkpoints?.map { it.name } ?: throw Exception("Rules hasn't been generated")
        val checkpointsResults = CheckpointsResultsReader(folder, checkpointNames).read()
        val participantResults = ParticipantsResultsReader(folder, checkpointNames).read()
        val finalResults = checkpointsResults + participantResults
        results = finalResults
        model.loadResults(finalResults)
    }

    fun updateApplications() {
        model.loadApplications(applications)
    }

    fun updateResults(competition: Competition) {
        model.updateCompetition(competition)
    }
}