package gui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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
    fun drawHeader(yOffset: Dp, selected: List<Tab>): Tab? {
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
    open fun drawContent(yOffset: Dp): Tab? {
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

class TabWithTable<T>(
    name: String = "",
    nextTabs: MutableList<Tab> = mutableListOf(),
    parent: Tab? = null,
    table: Table<T>
) : Tab(
    name,
    nextTabs,
    parent,
    @Composable {
        var switch: Tab? by remember { mutableStateOf(null) }
        var refresh: Boolean by remember { mutableStateOf(false) }
        if (refresh) switch = null

        var showDelete by remember { mutableStateOf(false) }
        val selected by remember { mutableStateOf(mutableSetOf<Int>()) }

        val update by remember { mutableStateOf(Update(this)) }
        val stateVertical = rememberScrollState(0)

        Row {
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
                    if (showDelete) {
                        table.delete(selected)
                        selected.clear()
                    }
                    showDelete = !showDelete
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = if (showDelete) Color.Red else Color.Blue)
            ) {
                Text("delete")
            }
        }

        if (showDelete) {
            Column(modifier = Modifier.padding(top = 40.dp).verticalScroll(stateVertical)) {
                repeat(table.size) {
                    var checked by remember { mutableStateOf(false) }
                    Checkbox(
                        checked = checked,
                        onCheckedChange = { check ->
                            println("$it, $check")
                            if (check)
                                selected.add(it)
                            else
                                selected.remove(it)
                            checked = check
                        },
                        modifier = Modifier.height(60.dp)
                    )
                }
            }
        }

        Box(modifier = Modifier.padding(start = if (showDelete) 40.dp else 0.dp, top = 40.dp)) {
            table.draw(stateVertical)
        }

        refresh = (switch != null)
        switch
    }
) {

}

class Update(parent: Tab?) : Tab(parent = parent, content = {}) {
    @Composable
    override fun drawContent(yOffset: Dp): Tab? = parent
}