import classes.Config
import parsers.readListOfIncompleteCheckpointsFromDirectoryWithCheckPointsResults
import parsers.readListOfTeamsFromDirectory

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
}


class ShellController(model: Model, private val config: Config) : Controller(model) {
    fun downloadApplications() {
        val listOfTeams = readListOfTeamsFromDirectory(config.applicationsFolder)
        model.uploadApplications(listOfTeams)
    }

    fun uploadResults() {
        val checkpointsResults = readListOfIncompleteCheckpointsFromDirectoryWithCheckPointsResults(
            config.checkpointsFolder, config.checkPoints
        )
        val participantResults = readListOfIncompleteCheckpointsFromDirectoryWithCheckPointsResults(
            config.participantsFolder, config.checkPoints
        )
        val finalResults = checkpointsResults + participantResults
        model.uploadResults(finalResults)
    }
}