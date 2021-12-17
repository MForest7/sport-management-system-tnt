package gui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import logger

class Table {

    @Composable
    fun drawTable(columnNames: List<String>, tableData: List<List<String>>) {
        logger.debug { "drawing table ($columnNames, $tableData)" }
        val backgroundColor = Color.LightGray
        val countOfColumns = columnNames.size

        tableData.forEach { require(it.size == countOfColumns) { "Wrong count of columns in table data($columnNames\n $tableData)" } }

        LazyColumn(Modifier.fillMaxWidth().padding(16.dp).offset(y = 40.dp)) {
            item {
                Row(Modifier.background(backgroundColor)) {
                    columnNames.forEach {
                        TextField(
                            value = it,
                            onValueChange = {}
                        )
                    }
                }
            }
            for (i in tableData.indices) {
                item {
                    Row(Modifier.background(backgroundColor)) {
                        for (s in tableData[i]) {
                            val curText = remember { mutableStateOf(s) }

                            TextField(
                                value = curText.value,
                                onValueChange = { curText.value = it }
                            )
                        }
                    }
                }
            }
        }
    }
}
