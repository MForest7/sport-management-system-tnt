package gui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.sqrt

class Column<T>(val name: String, val changeable: Boolean, val get: T.() -> Any, val set: T.(String) -> Unit)

open class Table<T>(
    protected var columns: List<Column<T>>,
    protected var tableData: MutableList<T>,
) {
    val size: Int
        get() = tableData.size

    private fun sortBy(columnIndex: Int) {
        tableData = tableData.sortedBy { it.(columns[columnIndex].get)() as Comparable<Any> }.toMutableList()
    }

    @Composable
    fun drawHeader(): Boolean {

        var switch: Boolean by remember { mutableStateOf(false) }
        var refresh: Boolean by remember { mutableStateOf(false) }
        if (refresh) switch = false

        var showDelete by remember { mutableStateOf(false) }
        val selected by remember { mutableStateOf(mutableSetOf<Int>()) }

        val stateVertical = rememberScrollState(0)

        if (this is MutableTable) {
            Row {
                Button(
                    onClick = {
                        add()
                        switch = true
                    }
                ) {
                    Text("add")
                }
                Button(
                    onClick = {
                        if (showDelete) {
                            delete(selected)
                            selected.clear()
                            switch = true
                        }
                        showDelete = !showDelete
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = if (showDelete) Color.Red else Color.Blue)
                ) {
                    Text("delete")
                }
            }
        }

        if (showDelete) {
            Column(modifier = Modifier.padding(top = 80.dp).verticalScroll(stateVertical)) {
                repeat(size) {
                    var checked by remember { mutableStateOf(false) }
                    Checkbox(
                        checked = checked,
                        onCheckedChange = { check ->
                            if (check)
                                selected.add(it)
                            else
                                selected.remove(it)
                            checked = check
                        },
                        modifier = Modifier.height(50.dp)
                    )
                }
            }
        }

        Box(
            modifier = Modifier.padding(start = if (showDelete) 40.dp else 0.dp, top = 40.dp)
        ) {
            if (drawTable(stateVertical))
                switch = true
        }

        refresh = (switch)
        return switch
    }

    @Composable
    fun drawTable(stateVertical: ScrollState): Boolean {
        var switch: Boolean by remember { mutableStateOf(false) }
        var refresh: Boolean by remember { mutableStateOf(false) }
        if (refresh) switch = false

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            columns.forEachIndexed { index, it ->
                Button(
                    onClick = {
                        sortBy(index)
                        switch = true
                    },
                    modifier = Modifier.weight(sqrt(it.name.length.toFloat()) + 1f)
                ) {
                    Text(it.name)
                }
            }
        }

        Column(
            modifier = Modifier.padding(top = 40.dp).verticalScroll(stateVertical).fillMaxWidth()
        ) {
            tableData.forEachIndexed { index, row ->
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    columns.forEach { column ->
                        var curText: String by remember { mutableStateOf(tableData[index].(column.get)().toString()) }
                        TextField(
                            value = curText,
                            onValueChange = { value ->
                                tableData[index].(column.set)(value)
                                curText = value
                            },
                            readOnly = !column.changeable,
                            modifier = Modifier.weight(sqrt(column.name.length.toFloat()) + 1f).height(50.dp)
                        )
                    }
                }
            }
        }

        refresh = switch
        return switch
    }
}

class MutableTable<T>(
    columns: List<Column<T>>,
    tableData: MutableList<T>,
    private val delete: (T) -> Unit,
    private val add: () -> T
) : Table<T>(
    columns = columns,
    tableData = tableData
) {
    fun add() {
        tableData.add(add.invoke())
    }

    fun delete(indices: Set<Int>) {
        val toDelete = tableData.filterIndexed { index, _ -> index in indices }
        tableData = tableData.filterIndexed { index, _ -> index !in indices }.toMutableList()
        toDelete.forEach { delete.invoke(it) }
    }
}
