package gui

import DB
import DatabaseController
import Model
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.unit.dp
import classes.*
import gui.Tables.checkpointsTable
import startShell

object GUI {
    //val db: CompetitionDB = TODO()

    val database = DB("database")
    val model = Model()
    val controller = DatabaseController(database, model)

    init {
        database.cleanCompetitors()
        database.clearCompetition()
        getCompetitionForTests().competitors.forEach {
            val id = database.createEmptyCompetitor(it.team)
            val competitor = Competitor(
                it.wishGroup,
                it.surname,
                it.name,
                it.birth,
                it.title,
                it.medicalExamination,
                it.medicalInsurance,
                id
            )
            database.updateCompetitor(competitor)
        }
        database.setCompetition(getCompetitionForTests())
        database.getAllApplications().teams/*.map {
            it.competitors.map { competitor -> CompetitorWithTeam(it, competitor) }
        }.flatten().toMutableList()*/.forEach {
            println(it)
        }
    }

    lateinit var myFileChooser: MyFileChooser

    @Composable
    fun run(window: ComposeWindow) {
        if (!GUI::myFileChooser.isInitialized)
            myFileChooser = MyFileChooser(window)

        var currentTab by remember { mutableStateOf(HOME) }

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
    }

    private val CHECKPOINTS = TabWithTable(
        name = "checkpoints",
        table = checkpointsTable()
    )

    private val LOAD_CONFIG = Tab.Builder("Load config")
        .withContent @Composable {
            try {
                startShell(myFileChooser.pickFile("json"), model)
            } catch (e: Exception) {
                MyErrorDialog.exception = Exception(e.message)
            }
            parent
        }
        .build()

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
        .withTabs(database.getAllApplications().teams.map { tabOfTeam(it) })
        .build()

    private val SORTITION = Tab.Builder("Sortition")
        .withTabs(database.getPossibleGroupNames().map { tabOfStartForGroup(Group(it, mutableListOf())) })
        .withContent {
            Button(
                onClick = {
                    controller.uploadApplications()
                    controller.generateSortition()
                }
            ) {
                Text("Generate")
            }
        }
        .build()

    private val HOME = Tab.Builder("HOME")
        .withTabs(LOAD_CONFIG, APPLICATIONS, SORTITION)
        .build()

    private fun tabOfTeam(team: Team) = TabWithTable<Competitor>(
        name = team.name,
        table = Tables.teamTable(team)
    )

    private fun tabOfStartForGroup(group: Group) = TabWithTable<CompetitorInCompetition>(
        name = group.name,
        table = Tables.groupTable(group)
    )

    private fun tabOfCheckpointsForGroup(group: Group) = TabWithTable<CheckPoint>(
        name = "checkpoints",
        table = checkpointsTable(group)
    )

    /*fun attachNewTab(parent: Tab?, name: String, content: Tab.() -> Unit) {
        val tab = Tab(name = name, content = content)
        parent.
    }*/
}