import mu.KotlinLogging
import parsers.readJSONConfig

val logger = KotlinLogging.logger { }

fun outputStringWithColor(string: String) {
    val color = "\u001B[36m" //cyan
    val reset = "\u001B[0m"
    print(color + string + reset + "\n")
}

fun startShell() {
    logger.info { "Start shell" }
    outputStringWithColor("Enter pat h to config(.json) file")
    outputStringWithColor("For example: myData/myconfig.json")
    val pathToConfig = getPathToConfig()
    val config = readJSONConfig(pathToConfig)
    logger.debug { "config = $config" }
    val model = Model()
    val viewer = ShellViewer(config)
    val controller = ShellController(model, config)
    model.addViewer(viewer)
    controller.downloadApplications()
    controller.generateSortition()

    print("Enter if results uploaded")
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
