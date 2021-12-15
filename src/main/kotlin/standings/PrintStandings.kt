package standings

import classes.CsvWriter


class GroupStandingsPrinter(val file: String) {
    private val writer = CsvWriter(file)

    private fun standingsForSingleGroup(standings: StandingsOfGroup): List<List<String>> {
        val headers = listOf(
            listOf(standings.group.name) + List(10) { "" },
            listOf("№ п/п", "Номер", "Фамилия", "Имя", "Г.р.", "Разр.", "Команда", "Результат", "Место", "Отставание")
        )
        val content = standings.records.mapIndexed { index, it ->
            listOf("${index + 1}") + it.competitor.getInfo() + listOf(it.time, it.place, it.gap)
        }

        return headers + content
    }

    fun print(standings: StandingsInGroups) {
        val header = listOf(listOf("Протокол результатов.") + List(9) { "" })

        val content = standings.standings.flatMap { standingsInGroup ->
            standingsForSingleGroup(standingsInGroup)
        }

        writer.writeRows(header + content)
    }
}


class TeamsStandingsPrinter(val file: String) {
    private val writer = CsvWriter(file)

    fun print(standings: StandingsInTeams) {
        val headers = listOf(
            listOf("Протокол результатов команд.") + List(3) { "" },
            listOf("№ п/п", "Команда", "Результат", "Место"),
        )
        val content = standings.standings.mapIndexed { index, it ->
            listOf(index + 1, it.team.name, it.points.toInt(), it.place).map {
                it.toString()
            }
        }
        writer.writeRows(headers + content)
    }
}