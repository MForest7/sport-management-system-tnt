package parsers

import classes.*

data class CompetitorRaw(
    val number: String,
    val surname: String,
    val name: String,
    val birth: String,
    val title: String,
    val startTime: Time
)

data class GroupFromOneFile(val name: String, val competitors: List<CompetitorRaw>)

class SortitionReader(override val dir: String, private val rules: Rules, private val application: Applications) :
    DirectoryReader<GroupFromOneFile, Competition> {

    override fun readUnit(csvReader: CsvReader): GroupFromOneFile {
        val groupName = csvReader.readFirstElementInFirstRow()
        val competitors = csvReader.readAllExceptFirst().map { line ->
            require(line.size == 6)
            CompetitorRaw(line[0], line[1], line[2], line[3], line[4], Time(line[5]))
        }
        return GroupFromOneFile(groupName, competitors)
    }

    override fun read(): Competition {
        val groupsFromFiles = readUnmerged()
        val groups = rules.groups
        val numberMatching = mutableMapOf<String, CompetitorInCompetition>()
        val checkpoint = CheckPoint("", mutableMapOf())
        val competitors = application.teams.flatMap { team ->
            team.competitors.map { competitor ->

                var groupFromFile: GroupFromOneFile? = null
                var number: String? = null
                var startTime: Time? = null

                for (groupFromOneFile in groupsFromFiles) {
                    groupFromOneFile.competitors.forEach { competitorRaw ->
                        if (competitorRaw.surname == competitor.surname &&
                            competitorRaw.name == competitor.name &&
                            competitorRaw.birth == competitor.birth &&
                            competitorRaw.title == competitor.title
                        ) {
                            require(groupFromFile == null) { "one competitor in many files\n$($competitor)" }
                            groupFromFile = groupFromOneFile
                            number = competitorRaw.number
                            startTime = competitorRaw.startTime
                        }
                    }
                }
                val groupFromFileNotNull = groupFromFile
                val numberNotNull = number
                val startTimeNotNull = startTime

                require(groupFromFileNotNull != null && numberNotNull != null && startTimeNotNull != null) { "cant find group from one of competitors\n(${competitor.surname} ${competitor.name}))" }

                val group = groups.find { it.name == groupFromFileNotNull.name }
                require(group != null) { "cant find group from one of competitors2\n($competitor))" }

                val competitorInCompetition = CompetitorInCompetition(competitor, numberNotNull, group, team)
                numberMatching[numberNotNull] = competitorInCompetition
                checkpoint.timeMatching[competitorInCompetition] = mutableListOf(startTimeNotNull)
                competitorInCompetition
            }
        }

        return Competition(listOf(checkpoint) + rules.checkpoints, competitors, numberMatching)
    }
}

