package gui

import GUIController
import GUIViewer
import Model
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import classes.Competitor
import classes.CompetitorInCompetition
import classes.Group
import classes.Team
import gui.Tables.checkpointsTable
import gui.Tables.showConfig
import gui.Tables.standingsInGroupTable
import gui.Tables.standingsTeams
import sortition.SortitionPrinter

object GUI {
    //val db: CompetitionDB = TODO()

    val model = Model()
    val controller = GUIController(model)
    val viewer = GUIViewer()

    init {
        model.addViewer(viewer)
    }

    lateinit var myFileChooser: MyFileChooser

    @Composable
    fun run(window: ComposeWindow) {
        if (!GUI::myFileChooser.isInitialized)
            myFileChooser = MyFileChooser(window)

        var currentTab by remember { mutableStateOf(HOME) }

        controller.updateApplications()
        viewer.competition?.let { controller.updateResults(it) }

        Button(
            onClick = { println("Back"); currentTab = currentTab.parent ?: currentTab; println(currentTab.name) },
        ) {
            Text("Back")
        }

        println(currentTab.getStack().map { it.name })
        /*currentTab.getStack().forEachIndexed { index, it ->
            currentTab = it.drawHeader((index * 40 + 40).dp, currentTab.getStack()) ?: currentTab
        }*/
        currentTab = currentTab.drawHeader((40).dp, currentTab.getStack()) ?: currentTab
        currentTab =
            currentTab.drawContent((if (currentTab.nextTabs.isEmpty()) 0 else 40).dp + 40.dp)
                ?: currentTab

        MyErrorDialog.show()
    }

    private val LOAD_CONFIG = Tab.Builder("Load config")
        .withContent @Composable {
            MyErrorDialog.tryToDo {
                controller.uploadGroups(myFileChooser.pickFileOrDir("json"))
            }
            parent
        }
        .build()

    private val SHOW_CONFIG = TabWithTable(
        name = "Show Config",
        table = showConfig(controller, viewer),
        content = {}
    )


    /*private val APPLICATIONS: TabWithTable<CompetitorWithTeam> = TabWithTable(
        name = "Applications",
        nextTabs = database.getAllApplications().teams.map { tabOfTeam(it) }.toMutableList(),
        table = Tables.applicationsTable(),
        content = @Composable {
            var selected by remember { mutableStateOf(false) }
            var text by remember { mutableStateOf("") }

            var switch: Tab? by remember { mutableStateOf(null) }
            var refresh: Boolean by remember { mutableStateOf(false) }
            val update by remember { mutableStateOf(Update(this)) }
            if (refresh) switch = null

            Button(
                onClick = {
                    if (selected) {
                        val team = Team(text, listOf())
                        addTab(TabWithTable(
                            name = text,
                            table = Tables.teamTable(team)
                        ))
                        switch = update
                    }
                    selected = !selected
                },
                modifier = Modifier.width(150.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green)
            ) {
                Text("New Team")
            }

            if (selected) {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    readOnly = false,
                    modifier = Modifier.padding(start = 150.dp).height(50.dp)
                )
            }

            refresh = (switch != null)
            switch
        }
    )*/

    private val APPLICATIONS = Tab.Builder("Applications")
        .withTabs(viewer.applications?.teams?.map { tabOfTeam(it) } ?: listOf())
        .withContent {
            var switch: Boolean by remember { mutableStateOf(false) }
            var refresh: Boolean by remember { mutableStateOf(false) }
            if (refresh) switch = false

            Button(
                onClick = {
                    MyErrorDialog.tryToDo {
                        controller.uploadApplications(myFileChooser.pickFileOrDir())
                    }
                    val tabs = (viewer.applications?.teams?.map { tabOfTeam(it) } ?: listOf<Tab>()).toMutableList()
                    this.nextTabs = tabs
                    tabs.forEach {
                        it.parent = this
                    }
                    switch = true
                },
            ) {
                Text("Load applications")
            }

            refresh = (switch)
            switch
        }
        .build()

    private val SORTITION = Tab.Builder("Sortition")
        .withTabs(viewer.rules?.groups?.map { tabOfStartForGroup(it) } ?: listOf())
        .withContent {
            var switch: Boolean by remember { mutableStateOf(false) }
            var refresh: Boolean by remember { mutableStateOf(false) }
            if (refresh) switch = false

            Row() {
                Button(
                    onClick = {
                        MyErrorDialog.tryToDo {
                            controller.updateApplications()
                            controller.generateSortition()
                            switch = true
                        }
                    }
                ) {
                    Text("Generate")
                }
                Button(
                    onClick = {
                        MyErrorDialog.tryToDo {
                            controller.uploadSortition(myFileChooser.pickFileOrDir())
                            switch = true
                        }
                    }
                ) {
                    Text("Load sortition")
                }
                Button(
                    onClick = {
                        MyErrorDialog.tryToDo {
                            val sortitionPrinter = SortitionPrinter(myFileChooser.pickFileOrDir(""))
                            val competition = viewer.competition
                            require(competition != null) { "competition is null" }
                            sortitionPrinter.print(competition)
                        }
                    }
                ) {
                    Text("Export sortition")
                }
            }

            if (switch) {
                val tabs = (viewer.rules?.groups?.map { tabOfStartForGroup(it) } ?: listOf<Tab>()).toMutableList()
                this.nextTabs = tabs
                tabs.forEach {
                    it.parent = this
                }
            }

            refresh = (switch)
            switch
        }
        .build()

    private val RESULTS_GROUPS = Tab.Builder("Results in groups")
        .withTabs(viewer.rules?.groups?.map { tabOfResultsForGroup(it) } ?: listOf())
        .build()

    private val RESULTS_TEAMS = tabOfResultsForTeams()

    private val CHECKPOINTS = Tab.Builder("checkpoints")
        .withGenTabs { (viewer.rules?.groups?.map { tabOfCheckpointsForGroup(it) } ?: listOf()).toMutableList() }
        .withContent {
            var switch: Boolean by remember { mutableStateOf(false) }
            var refresh: Boolean by remember { mutableStateOf(false) }
            if (refresh) switch = false

            Row() {
                Button(
                    onClick = {
                        MyErrorDialog.tryToDo {
                            controller.uploadResults(myFileChooser.pickFileOrDir())
                            switch = true
                        }
                    }
                ) {
                    Text("Load records")
                }
            }

            refresh = (switch)
            switch
        }
        .build()

    private val HOME = Tab.Builder("HOME")
        .withTabs(LOAD_CONFIG, SHOW_CONFIG, APPLICATIONS, SORTITION, CHECKPOINTS, RESULTS_GROUPS, RESULTS_TEAMS)
        .build()

    private fun tabOfTeam(team: Team) = TabWithTable<Competitor>(
        name = team.name,
        table = Tables.teamTable(team, controller, viewer)
    )

    private fun tabOfStartForGroup(group: Group) = TabWithTable<CompetitorInCompetition>(
        name = group.name,
        table = Tables.groupTable(group, controller, viewer),
        content = @Composable {
            TextField(
                value = group.name,
                modifier = Modifier.background(Color.Cyan).height(50.dp),
                onValueChange = {},
                readOnly = true
            )
        }
    )

    private fun tabOfCheckpointsForGroup(group: Group) = TabWithTable<CompetitorInCompetition>(
        name = group.name,
        table = checkpointsTable(group, controller, viewer),
        content = @Composable {
            TextField(
                value = group.name,
                modifier = Modifier.background(Color.Cyan).height(50.dp),
                onValueChange = {},
                readOnly = true
            )
        }
    )

    private fun tabOfResultsForGroup(group: Group) = TabWithTable(
        name = group.name,
        table = standingsInGroupTable(group, controller, viewer),
        content = @Composable {
            TextField(
                value = group.name,
                modifier = Modifier.background(Color.Cyan).height(50.dp),
                onValueChange = {},
                readOnly = true
            )
        }
    )

    private fun tabOfResultsForTeams() = TabWithTable(
        name = "Team results",
        table = standingsTeams(controller, viewer),
        content = {}
    )
}