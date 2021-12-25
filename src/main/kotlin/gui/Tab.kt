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
    var nextTabs: MutableList<Tab> = mutableListOf(),
    var parent: Tab? = null,
    val content: @Composable() Tab.() -> Any?,
    var genTabs: (() -> MutableList<Tab>)? = null
) {
    data class Builder(
        private var name: String,
        private var tabs: List<Tab> = listOf(),
        private var content: @Composable Tab.() -> Any? = {},
        private var genTabs: (() -> MutableList<Tab>)? = null
    ) {

        fun withTabs(tabs: List<Tab>) = apply { this.tabs = tabs }
        fun withTabs(vararg tabs: Tab) = apply { this.tabs = tabs.toList() }
        fun withGenTabs(gen: () -> MutableList<Tab>) = apply { this.genTabs = gen }
        fun withContent(content: @Composable Tab.() -> Any?) = apply { this.content = content }

        fun build(): Tab {
            val tab = Tab(name, tabs.toMutableList(), content = content, genTabs = genTabs)
            tabs.forEach {
                it.parent = tab
            }
            return tab
        }
    }

    fun addTab(vararg tabs: Tab) {
        tabs.forEach {
            it.parent = this
            nextTabs.add(it)
        }
    }

    init {
        nextTabs.forEach { it.parent = this }
    }

    fun getStack(): List<Tab> = (parent?.getStack() ?: listOf<Tab>()) + listOf(this)

    @Composable
    fun drawHeader(yOffset: Dp, selected: List<Tab>): Tab? {
        nextTabs = genTabs?.invoke() ?: nextTabs
        nextTabs.forEach { it.parent = this }

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
    private var table: Table<T>,
    content: @Composable Tab.() -> Any? = {}
) : Tab(
    name,
    nextTabs,
    parent,
    content
) {
    @Composable
    override fun drawContent(yOffset: Dp): Tab? {
        var switch: Tab? by remember { mutableStateOf(null) }
        var refresh: Boolean by remember { mutableStateOf(false) }
        val update by remember { mutableStateOf(Update(this)) }
        if (refresh) switch = null

        Box(modifier = Modifier.padding(top = yOffset)) {
            val checkSwitch = content.invoke(this@TabWithTable)
            if (checkSwitch is Tab) switch = checkSwitch
            val checkUpdate = table.drawHeader()
            table.print()
            if (checkUpdate) switch = update
        }

        refresh = (switch != null)
        return switch
    }
}

class Update(parent: Tab?) : Tab(parent = parent, content = {}) {
    @Composable
    override fun drawContent(yOffset: Dp): Tab? = parent
}