package gui

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp

object GUI {
    //val db: CompetitionDB = TODO()

    @Composable
    fun run() {
        var currentTab by remember { mutableStateOf(MAIN) }

        Button(
            onClick = { println("Back"); currentTab = currentTab.parent ?: currentTab; println(currentTab.name) },
        ) {
            Text("Back")
        }

        println(currentTab.getStack().map { it.name })
        currentTab.getStack().forEachIndexed { index, it ->
            currentTab = it.draw((index * 40 + 40).dp, currentTab.getStack()) ?: currentTab
        }
        currentTab = currentTab.drawContent((currentTab.getStack().size * 40 + 40).dp) ?: currentTab
    }

    /*fun attachNewTab(parent: Tab?, name: String, content: Tab.() -> Unit) {
        val tab = Tab(name = name, content = content)
        parent.
    }*/
}