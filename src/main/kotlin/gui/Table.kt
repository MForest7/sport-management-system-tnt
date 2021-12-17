package gui

import CompetitorsDB
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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

class Table {

    @Composable
    fun drawTable(columnNames: List<String>, tableData: List<List<String>>, db: CompetitorsDB) {

        val realData = tableData.map { it.toMutableList() }.toMutableList()
        logger.debug { "drawing table ($columnNames, $realData)" }
        val backgroundColor = Color.LightGray
        val countOfColumns = columnNames.size

        val rowSize = remember { mutableStateOf(IntSize.Zero) }

        realData.forEach { require(it.size == countOfColumns) { "Wrong count of columns in table data($columnNames\n $realData)" } }
        require(columnNames[0] == "id") { "first column is not an id" }

        LazyColumn(Modifier.fillMaxWidth().padding(16.dp).offset(y = 40.dp).onSizeChanged {
            rowSize.value = it
        }) {
            item {
                Row(Modifier.fillMaxWidth().background(backgroundColor)) {
                    columnNames.indices.forEach { index ->
                        TextField(
                            value = columnNames[index],
                            readOnly = true,
                            onValueChange = {},
                            modifier = Modifier.width((rowSize.value.width / countOfColumns).dp)
                        )
                    }
                }
            }
            for (i in realData.indices) {
                item {
                    Row(Modifier.background(backgroundColor)) {
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
                                }, readOnly = j == 0,
                                modifier = Modifier.width((rowSize.value.width / countOfColumns).dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
