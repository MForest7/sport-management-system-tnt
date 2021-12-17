package gui

import CompetitorsDB
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import logger

class Table(
    private var columnNames: List<String>,
    private var tableData: List<List<String>>,
    private val db: CompetitorsDB
) {
    @Composable
    fun draw(offsetX: Dp, offsetY: Dp, showDelete: Boolean) {
        if (showDelete)
            showDeleteButtons(offsetX, offsetY)
        else
            drawTable(offsetX, offsetY, false)
    }

    @Composable
    fun drawTable(offsetX: Dp, offsetY: Dp, showDelete: Boolean) {
        val realData = tableData.map { it.toMutableList() }.toMutableList()
        logger.debug { "drawing table ($columnNames, $realData)" }
        val backgroundColor = Color.LightGray
        val deleteButtonBackground = Color.Gray
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
                        Button(onClick = {println("kek")}, modifier = Modifier.background(Color.LightGray).width((rowSize.value.width / countOfColumns).dp)) {
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
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                                onClick = {
                                    db.deleteCompetitor(realData[i][0].toInt())
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
                                }, readOnly = j == 0,
                                modifier = Modifier.width((rowSize.value.width / countOfColumns).dp)
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun showDeleteButtons(offsetX: Dp, offsetY: Dp) {
        val offsetButton = 20.dp
        drawTable(offsetX + offsetButton, offsetY, false)

        val realData = tableData.map { it.toMutableList() }.toMutableList()
        val backgroundColor = Color.Red

        LazyColumn(Modifier.width(offsetButton)) {
            for (i in realData.indices) {
                item {
                    Row(Modifier.background(backgroundColor).width(offsetButton)) {
                        Button( onClick = {
                            db.deleteCompetitor(realData[i][0].toInt())
                        } ) {
                            Text("Delete competitor")
                        }
                    }
                }
            }
        }
    }
}
