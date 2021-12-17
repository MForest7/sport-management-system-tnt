import classes.FileManager
import mu.KotlinLogging
import parsers.JsonReader
import parsers.readGroups

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
    val config = JsonReader(pathToConfig).read()
    logger.debug { "config = $config" }
    val fileManager = FileManager(config)
    val model = Model()
    val viewer = ShellViewer(fileManager)
    val controller = ShellController(model, fileManager)
    model.addViewer(viewer)
    controller.downloadGroups(readGroups(config.groups))
    controller.downloadApplications()
    controller.generateSortition()

    outputStringWithColor("Press enter if results were uploaded")
    readLine()

    controller.uploadResults()
    controller.generateStandingsInGroups()
    controller.generateStandingsInTeams()

    outputStringWithColor("Done!")
    logger.info { "End shell" }
}


fun getPathToConfig(): String {
    val pathToConfig = readLine()
    require(pathToConfig != null) { "path is null" }
    require(pathToConfig.endsWith(".json")) { "Not a .json file" }
    return pathToConfig
}
