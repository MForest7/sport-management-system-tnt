package gui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.sqrt

class Column<T>(val name: String, val changeable: Boolean, val get: T.() -> Any, val set: T.(String) -> Unit)

data class NameOnly(var value: String)

open class Table<T>(
    protected var columns: List<Column<T>>,
    protected var tableData: MutableList<T>,
) {
    val size: Int
        get() = tableData.size

    fun sortBy(columnIndex: Int) {
        tableData = tableData.sortedBy { it.(columns[columnIndex].get)() as Comparable<Any> }.toMutableList()
    }

    @Composable
    fun draw(stateVertical: ScrollState, showDelete: Boolean): Boolean {
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
                    modifier = Modifier.weight(sqrt(it.name.length.toFloat()))
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
                                val oldValue = tableData[index]
                                tableData[index].(column.set)(value)

                                curText = value
                            },
                            readOnly = !column.changeable,
                            modifier = Modifier.weight(sqrt(column.name.length.toFloat()))
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
    /*private val controller : DatabaseController,*/
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

/*class Table<T>(private var data: MutableList<T>, private val default: T) {
    fun add() {
        data.add(default)
    }

    fun delete(indices: Set<Int>) {
        data = data.filterIndexed { index, t -> index !in indices }.toMutableList()
    }

    val size: Int
        get() = data.size

    @Composable
    fun draw(stateVertical: ScrollState) {
        Column(modifier = Modifier.verticalScroll(stateVertical)) {
            data.forEach {
                TextField(
                    it.toString(),
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.height(60.dp)
                )
            }
        }
    }
}*/

/*class Table(
    private var columnNames: List<String>,
    private var tableData: List<List<String>>,
    private val db: CompetitorsDB
) {

    @Composable
    fun drawTable(
        offsetX: Dp,
        offsetY: Dp,
        showDelete: Boolean,
        selected: MutableSet<Int>,
        columnToSort: MutableState<Int>,
        lastButton: MutableState<MyButtons>,
        isMutable: Boolean
    ) {
        val realData = tableData.map { it.toMutableList() }.toMutableList()
        logger.debug { "drawing table ($columnNames, $realData)" }
        val backgroundColor = Color.LightGray
        val countOfColumns = columnNames.size

        val buttonWidth = 30.dp

        val rowSize = remember { mutableStateOf(IntSize.Zero) }

        realData.forEach { require(it.size == countOfColumns) { "Wrong count of columns in table data($columnNames\n $realData)" } }
        require(columnNames[0] == "id") { "first column is not an id" }

        LazyColumn(Modifier.fillMaxWidth().padding(16.dp).offset(x = offsetX, y = offsetY).onSizeChanged {
            rowSize.value = it
        }) {
            item {
                Row(Modifier.fillMaxWidth().background(backgroundColor)) {
                    columnNames.indices.forEach { index ->
                        Button(
                            onClick = {
                                columnToSort.value = index
                                lastButton.value = MyButtons.SORTED_BY_COLUMN
                            },
                            modifier = Modifier.background(Color.LightGray)
                                .width((rowSize.value.width / countOfColumns).dp)
                        ) {
                            Text(text = columnNames[index])
                        }
                    }
                }
            }

            for (i in realData.indices) {
                item {
                    Row(Modifier.background(backgroundColor)) {
                        if (showDelete) {
                            Button(
                                modifier = Modifier.width(buttonWidth),
                                colors = ButtonDefaults.buttonColors(backgroundColor = if (realData[i][0].toInt() in selected) Color.Red else Color.Gray),
                                onClick = {
                                    val id = realData[i][0].toInt()
                                    if (id in selected)
                                        selected.remove(id)
                                    else
                                        selected.add(id)
                                    lastButton.value = MyButtons.SELECT_ROW
                                }) {
                                Text("X")
                            }
                        }

                        for (j in realData[i].indices) {
                            val curText = remember { mutableStateOf(realData[i][j]) }

                            TextField(
                                value = curText.value,
                                onValueChange = {
                                    curText.value = it
                                    realData[i][j] = it

                                    db.updateCompetitor(
                                        realData[i][0].toInt(),
                                        columnNames.drop(1).zip(realData[i].drop(1)).toMap()
                                    )
                                }, readOnly = (j == 0) || !isMutable,
                                modifier = Modifier.width((rowSize.value.width / countOfColumns).dp)
                            )
                        }
                    }
                }
            }
        }
    }
}*/
