package gui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import androidx.compose.ui.window.singleWindowApplication
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import classes.Applications
import classes.Competition
import classes.IncompleteCompetition
import classes.Rules
import standings.StandingsInGroups
import standings.StandingsInTeams

object GUI {
    //val db: CompetitionDB = TODO()

    @Composable
    fun run() {
        var currentTab by remember { mutableStateOf(Tabs.MAIN.content) }

        Button(
            onClick = { println("Back"); currentTab = currentTab.parent ?: currentTab; println(currentTab.name) },
        ) {
            Text("Back")
        }

        println(currentTab.getStack().map { it.name })
        currentTab.getStack().forEachIndexed { index, it -> currentTab = it.draw((index * 40 + 40).dp, currentTab.getStack()) ?: currentTab }
        currentTab.drawContent((currentTab.getStack().size * 40 + 40).dp)
    }

    class Tab(
        val name: String = "",
        val nextTabsEnum: List<Tabs> = listOf<Tabs>(),
        var parent: Tab? = null,
        val content: @Composable() Tab.() -> Unit
    ) {
        private val nextTabs = nextTabsEnum.map { it.content }

        init { nextTabs.forEach { it.parent = this } }

        fun getStack(): List<Tab> = (parent?.getStack() ?: listOf<Tab>()) + listOf(this)

        @Composable
        fun draw(yOffset: Dp, selected: List<Tab>): Tab? {
            var switch: Tab? by remember { mutableStateOf(null) }
            Row(
                modifier = Modifier.padding(top = yOffset).fillMaxWidth(),
            ) {
                nextTabs.forEach {
                    Button(
                        onClick = { switch = it },
                        colors = ButtonDefaults.buttonColors(if (it in selected) Color.Cyan else Color.Blue)
                    ) {
                        Text(text = it.name)
                    }
                }
            }
            val go = switch
            switch = null
            return go
        }

        @Composable
        fun drawContent(yOffset: Dp) {
            Box(modifier = Modifier.padding(top = yOffset)) {
                content.invoke(this@Tab)
            }
        }
    }
}