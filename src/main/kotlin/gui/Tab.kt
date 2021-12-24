package gui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

open class Tab(
    val name: String = "",
    val nextTabs: MutableList<Tab> = mutableListOf(),
    var parent: Tab? = null,
    val content: @Composable() Tab.() -> Any?
) {
    data class Builder(
        private var name: String,
        private var tabs: List<Tab> = listOf(),
        private var content: @Composable Tab.() -> Any? = {}
    ) {

        fun withTabs(tabs: List<Tab>) = apply { this.tabs = tabs }
        fun withContent(content: @Composable Tab.() -> Any?) = apply { this.content = content }

        fun build(): Tab {
            val tab = Tab(name, tabs.toMutableList(), content = content)
            tabs.forEach {
                it.parent = tab
            }
            return tab
        }
    }

    fun addTab(tab: Tab) {
        tab.parent = this
        nextTabs.add(tab)
    }

    init {
        nextTabs.forEach { it.parent = this }
    }

    fun getStack(): List<Tab> = (parent?.getStack() ?: listOf<Tab>()) + listOf(this)

    @Composable
    fun draw(yOffset: Dp, selected: List<Tab>): Tab? {
        var switch: Tab? by remember { mutableStateOf(null) }
        var refresh: Boolean by remember { mutableStateOf(false) }
        if (refresh) switch = null

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

        refresh = (switch != null)
        return switch
    }

    @Composable
    fun drawContent(yOffset: Dp): Tab? {
        var switch: Tab? by remember { mutableStateOf(null) }
        var refresh: Boolean by remember { mutableStateOf(false) }
        if (refresh) switch = null

        Box(modifier = Modifier.padding(top = yOffset)) {
            val checkSwitch = content.invoke(this@Tab)
            if (checkSwitch is Tab) switch = checkSwitch
        }

        refresh = (switch != null)
        return switch
    }
}

class TabWithAddDelete(
    name: String = "",
    nextTabs: MutableList<Tab> = mutableListOf(),
    parent: Tab? = null,
    table: Table
) : Tab(
    name,
    nextTabs,
    parent,
    @Composable {
        val update by remember { mutableStateOf(Tab(parent = this) {}) }
        var switch: Tab? by remember { mutableStateOf(null) }
        var refresh: Boolean by remember { mutableStateOf(false) }
        if (refresh) switch = null

        //val showDelete

        Button(
            onClick = {
                table.add()
                switch = update
            }
        ) {
            Text("add")
        }
        Button(
            onClick = {

            }
        ) {
            Text("delete")
        }

        refresh = (switch != null)
        switch
    }
) {

}