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

    fun uploadSortition() {

    }

    abstract fun downloadApplications()

    abstract fun uploadResults()
}


class ShellController(model: Model, private val fileManager: FileManager) : Controller(model) {
    fun downloadGroups(listOfGroups: List<Group>) {
        model.uploadGroups(listOfGroups)
    }

    override fun downloadApplications() {
        val listOfTeams = fileManager.applicationsReader.read()
        model.uploadApplications(listOfTeams)
    }

    override fun uploadResults() {
        val checkpointsResults = fileManager.resultsForCheckpointsReader.read()
        val participantResults = fileManager.resultsForParticipantsReader.read()
        val finalResults = checkpointsResults + participantResults
        model.uploadResults(finalResults)
    }
}