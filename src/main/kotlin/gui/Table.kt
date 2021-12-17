package gui

import CompetitorsDB
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import logger

fun mySort(data: List<MutableList<String>>, index: Int): List<MutableList<String>> {
    if (data.find { it[index].toIntOrNull() == null } == null) {
        return data.sortedBy { it[index].toInt() }
    }
    return data.sortedBy { it[index] }
}

class Table {

    @Composable
    fun drawTable(columnNames: List<String>, tableData: List<List<String>>, db: CompetitorsDB) {

        val realData = remember { mutableStateOf(tableData.map { it.toMutableList() }.toMutableList()) }
        logger.debug { "drawing table ($columnNames, $realData)" }
        val backgroundColor = Color.LightGray
        val countOfColumns = columnNames.size

        val rowSize = remember { mutableStateOf(IntSize.Zero) }

        realData.value.forEach { require(it.size == countOfColumns) { "Wrong count of columns in table data($columnNames\n $realData)" } }
        require(columnNames[0] == "id") { "first column is not an id" }

        val redraw = remember { mutableStateOf(false) }

        LazyColumn(Modifier.fillMaxWidth().padding(16.dp).offset(y = 40.dp).onSizeChanged {
            rowSize.value = it
        }) {
            item {
                Row(Modifier.fillMaxWidth().background(backgroundColor)) {
                    columnNames.indices.forEach { index ->
                        Button(
                            onClick = {
                                redraw.value = true
                                realData.value = mySort(realData.value, index).toMutableList()
                            },
                            modifier = Modifier.width((rowSize.value.width / countOfColumns).dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor)
                        ) {
                            Text(text = columnNames[index])
                        }
                    }
                }
            }
            for (i in realData.value.indices) {
                item {
                    Row(Modifier.background(backgroundColor)) {
                        for (j in realData.value[i].indices) {
                            val curText = remember { mutableStateOf(realData.value[i][j]) }

                            TextField(
                                value = curText.value,
                                onValueChange = {
                                    curText.value = it
                                    realData.value[i][j] = it
                                    db.updateCompetitor(
                                        realData.value[i][0].toInt(),
                                        columnNames.drop(1).zip(realData.value[i].drop(1)).toMap()
                                    )
                                }, readOnly = (j == 0),
                                modifier = Modifier.width((rowSize.value.width / countOfColumns).dp)
                            )
                        }
                    }
                }
            }
        }
        if (redraw.value) {
            drawTable(columnNames, realData.value, db)
        }
    }
}
