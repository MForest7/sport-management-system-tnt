package classes

import parsers.ApplicationsReader
import parsers.CheckpointsResultsReader
import parsers.ParticipantsResultsReader
import sortition.SortitionPrinter
import standings.GroupStandingsPrinter
import standings.TeamsStandingsPrinter
import java.io.File
import java.lang.Exception

class FileManagerException(message: String) : Exception(message)


class FileManager(
    config: Config
) {
    private fun checkDir(dir: String) {
        if (!File(dir).exists())
            File(dir).mkdir()
    }

    private fun checkFile(file: String) {
        if (File(file).exists()) {
            throw FileManagerException("File $file already exists.")
        }
    }

    init {
        checkDir(config.applicationsFolder)
        checkDir(config.sortitionFolder)
        checkFile(config.resultsInTeams)
        checkFile(config.resultsInGroups)
        checkDir(config.checkpointsFolder)
        checkDir(config.checkpointsFolder)
    }

    val applicationsReader = ApplicationsReader(config.applicationsFolder)
    val sortitionWriter = SortitionPrinter(config.sortitionFolder)
    val resultsForCheckpointsReader =
        CheckpointsResultsReader(config.checkpointsFolder, config.checkPoints)
    val resultsForParticipantsReader =
        ParticipantsResultsReader(config.participantsFolder, config.checkPoints)
    val standingsInGroupsWriter = GroupStandingsPrinter(config.resultsInGroups)
    val standingsInTeamsWriter = TeamsStandingsPrinter(config.resultsInTeams)
}