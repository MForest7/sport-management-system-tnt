import classes.Config
import mu.KotlinLogging
import parsers.readJSONConfig
import parsers.readListOfTeamsFromDirectory
import sortition.generateSortition
import sortition.printSortition
import standings.printStandingsInGroupsToDir
import standings.printStandingsInTeamsToDir

val logger = KotlinLogging.logger { }

fun outputStringWithColor(string: String) {
    val color = "\u001B[36m" //cyan
    val reset = "\u001B[0m"
    print(color + string + reset + "\n")
}

fun startShell() {
    logger.info {"Start shell"}
    outputStringWithColor("Enter path to config(.json) file")
    outputStringWithColor("For example: myData/myconfig.json")
    val pathToConfig = getPathToConfig()
    val config = readJSONConfig(pathToConfig)
    logger.debug {"config = $config"}
    startExecutingConfig(config)
    outputStringWithColor("Done!")
    logger.info {"End shell"}
}


fun getPathToConfig() : String {
    val pathToConfig = readLine()
    require(pathToConfig != null) {"path is null"}
    require(pathToConfig.endsWith(".json")) {"Not a .json file"}
    return pathToConfig
}

fun startExecutingConfig(config : Config) {
    require(config.applicationsFolder != null) { "applicationsFolder is null" }
    require(config.sortitionFolder != null) { "sortitionFolder is null" }
    val listOfTeams = readListOfTeamsFromDirectory(config.applicationsFolder)
    val competition = generateSortition(listOfTeams)
    logger.debug { "${config.mode} mode" }
    when(config.mode) {
        "Sortition" -> {
            printSortition(config.sortitionFolder, competition)
        }
        "Results in teams" -> {
            require(config.resultsInTeams != null) { "resultsInTeamsFolder is null" }
            require(config.resultsInTeams.endsWith(".csv")) {"resultsInTeamsFolder not a .csv file"}
            printStandingsInTeamsToDir(config.resultsInTeams, competition)
        }
        "Results in groups" -> {
            require(config.resultsInGroups != null) { "resultsInGroupsFolder is null" }
            require(config.resultsInGroups.endsWith(".csv")) {"resultsInGroupsFolder not a .csv file"}
            printStandingsInGroupsToDir(config.resultsInGroups, competition)
        }
    }
}
