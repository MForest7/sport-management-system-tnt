import classes.Config
import classes.Mode
import mu.KotlinLogging
import parsers.readJSONConfig
import parsers.readListOfTeamsFromDirectory
import sortition.generateSortition
import sortition.printSortition
import standings.StandingsInGroups
import standings.StandingsInTeams
import standings.printStandingsInGroupsToFile
import standings.printStandingsInTeamsToFile

val logger = KotlinLogging.logger { }

fun outputStringWithColor(string: String) {
    val color = "\u001B[36m" //cyan
    val reset = "\u001B[0m"
    print(color + string + reset + "\n")
}

fun startShell() {
    logger.info { "Start shell" }
    outputStringWithColor("Enter path to config(.json) file")
    outputStringWithColor("For example: myData/myconfig.json")
    val pathToConfig = getPathToConfig()
    val config = readJSONConfig(pathToConfig)
    logger.debug { "config = $config" }
    startExecutingConfig(config)
    outputStringWithColor("Done!")
    logger.info { "End shell" }
}


fun getPathToConfig(): String {
    val pathToConfig = readLine()
    require(pathToConfig != null) { "path is null" }
    require(pathToConfig.endsWith(".json")) { "Not a .json file" }
    return pathToConfig
}

fun startExecutingConfig(config: Config) {
    val listOfTeams = readListOfTeamsFromDirectory(config.applicationsFolder)
    val competition = generateSortition(listOfTeams)
    logger.debug { "${config.mode} mode" }
    if (config.mode != Mode.SORTITION) {
        competition.loadResults(config)
    }
    when (config.mode) {
        Mode.SORTITION -> {
            printSortition(config.sortitionFolder, competition)
        }
        Mode.RESULTS_TEAMS -> {
            require(config.resultsInTeams != null) { "Results in teams file is null" }
            require(config.resultsInTeams.endsWith(".csv")) { "resultsInTeamsFolder not a .csv file" }
            printStandingsInTeamsToFile(StandingsInTeams(competition), config.resultsInTeams)
        }
        Mode.RESULTS_GROUPS -> {
            require(config.resultsInGroups != null) { "Results in groups file is null" }
            require(config.resultsInGroups.endsWith(".csv")) { "resultsInGroupsFolder not a .csv file" }
            printStandingsInGroupsToFile(StandingsInGroups(competition), config.resultsInGroups)
        }
    }
}
