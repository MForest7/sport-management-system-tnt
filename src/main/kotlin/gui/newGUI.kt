package gui

import DB
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.unit.dp
import startShell

object GUI {
    //val db: CompetitionDB = TODO()

    val database = DB("database")
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
        currentTab.getStack().forEachIndexed { index, it ->
            currentTab = it.drawHeader((index * 40 + 40).dp, currentTab.getStack()) ?: currentTab
        }
        currentTab =
            currentTab.drawContent((currentTab.getStack().size * 40 + if (currentTab.nextTabs.isEmpty()) 0 else 40).dp)
                ?: currentTab
    }

    private val LOAD_CONFIG = Tab.Builder("Load config")
        .withContent @Composable {
            try {
                startShell(myFileChooser.pickFile("json"))
            } catch (e: Exception) {
                MyErrorDialog.exception = Exception(e.message)
            }
        }

    private val APPLICATIONS = TabWithTable<CompetitorWithTeam>(
        name = "Applications",
        table = Tables.applictionsTable()
    )

    private val HOME = Tab.Builder("HOME")
        .withTabs(APPLICATIONS)
        .build()

    /*fun attachNewTab(parent: Tab?, name: String, content: Tab.() -> Unit) {
        val tab = Tab(name = name, content = content)
        parent.
    }*/
}