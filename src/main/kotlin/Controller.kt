import classes.*
import parsers.ApplicationsReader
import parsers.CheckpointsResultsReader
import parsers.JsonReader
import parsers.ParticipantsResultsReader


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


class ShellController(model: Model, private val fileManager: FileManager) : Controller(model) {
    fun loadGroups(listOfGroups: List<Group>) {
        model.loadGroups(listOfGroups)
    }

    override fun uploadApplications() {
        val listOfTeams = fileManager.applicationsReader.read()
        model.loadApplications(listOfTeams)
    }

    override fun loadResults() {
        val checkpointsResults = fileManager.resultsForCheckpointsReader.read()
        val participantResults = fileManager.resultsForParticipantsReader.read()
        val finalResults = checkpointsResults + participantResults
        model.loadResults(finalResults)
    }
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

    fun updateResults() {

    }
}