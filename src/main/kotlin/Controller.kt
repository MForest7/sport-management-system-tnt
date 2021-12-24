import classes.FileManager
import classes.Group


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