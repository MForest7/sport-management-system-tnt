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

fun startShell(pathToConfig: String) {
    logger.info { "Start shell" }
    val config = JsonReader(pathToConfig).read()
    logger.debug { "config = $config" }

    val fileManager = FileManager(config)
    val model = Model()
    val viewer = ShellViewer(fileManager)
    val controller = ShellController(model, fileManager)
    model.addViewer(viewer)
    controller.loadGroups(readGroups(config.groups))
    controller.uploadApplications()
    controller.generateSortition()

    outputStringWithColor("Press enter if results were uploaded")
    readLine()

    controller.loadResults()
    controller.generateStandingsInGroups()
    controller.generateStandingsInTeams()

    outputStringWithColor("Done!")

    logger.info { "End shell" }
}

