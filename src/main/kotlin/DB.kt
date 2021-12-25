import classes.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

interface CompetitorsDB {
    /**
     * data keys format:
     * id, wishGroup, surname, team, name, birth, title
     */
    fun updateCompetitor(competitor: Competitor)
    fun createEmptyCompetitor(team: Team? = null): Int
    fun getCompetitor(id: Int): Competitor
    fun getAllCompetitors(): List<Competitor>
    fun getAllApplications(): Applications
    fun cleanCompetitors()
    fun deleteCompetitor(id: Int)
    fun setPossibleGroupNames(groups: List<String>)
    fun getPossibleGroupNames(): List<String>
}

interface SortitionDB {
    fun setCompetition(competition: Competition)
    fun getCompetition(): Competition?
    fun clearCompetition()
}


class DB(name: String) : CompetitorsDB, SortitionDB {

    object Competitors : Table("Applications") {
        val id = integer("id").autoIncrement().primaryKey()
        val wishGroup = text("wishGroup").nullable()
        val surname = text("surname").nullable()
        val team = text("team").nullable()
        val name = text("name").nullable()
        val birth = text("birth").nullable()
        val title = text("title").nullable()
    }

    object CompetitorsInCompetition : Table("CompetitorsInCompetition") {
        val id = integer("id").primaryKey()
        val wishGroup = text("wishGroup")
        val surname = text("surname")
        val team = text("team")
        val name = text("name")
        val birth = text("birth")
        val title = text("title")
        val number = text("number")
        val group = text("group")
        val groupCalculatorId = integer("groupCalculatorId")
        val checkpointNames = text("checkpointNames")
    }

    object Checkpoints : Table("Checkpoints") {
        val name = text("name")
        val data = text("data")
    }

    object PossibleGroups : Table("PossibleGroups") {
        val group = text("name")
    }

    private val db = Database.connect("jdbc:h2:./$name;DB_CLOSE_DELAY=-1;", "org.h2.Driver")

    init {
        transaction(db) {
            SchemaUtils.create(Competitors, CompetitorsInCompetition, Checkpoints, PossibleGroups)
        }
    }

    override fun updateCompetitor(competitor: Competitor) {
        transaction {
            Competitors.update({ Competitors.id eq competitor.id }) {
                it[surname] = competitor.surname
                it[name] = competitor.name
                it[title] = competitor.title
                it[birth] = competitor.birth
            }
        }
    }

    override fun createEmptyCompetitor(teamToAdd: Team?): Int {
        return transaction(db) {
            SchemaUtils.create(Competitors)
            Competitors.insert {
                it[wishGroup] = null
                it[surname] = null
                it[name] = null
                it[title] = null
                it[birth] = null
                it[team] = teamToAdd?.name
            } get Competitors.id
        }
    }

    override fun getCompetitor(id: Int): Competitor {
        return transaction(db) {
            Competitors.select { Competitors.id eq id }.map {
                Competitor(
                    it[Competitors.wishGroup] ?: "",
                    it[Competitors.surname] ?: "",
                    it[Competitors.name] ?: "",
                    it[Competitors.birth] ?: "",
                    it[Competitors.title] ?: "",
                    "",
                    "",
                    it[Competitors.id]
                )
            }.first()
        }
    }

    override fun getAllCompetitors(): List<Competitor> {
        return transaction(db) {
            Competitors.selectAll().map {
                Competitor(
                    it[Competitors.wishGroup] ?: "",
                    it[Competitors.surname] ?: "",
                    it[Competitors.name] ?: "",
                    it[Competitors.birth] ?: "",
                    it[Competitors.title] ?: "",
                    "",
                    "",
                    it[Competitors.id]
                )
            }
        }
    }

    override fun getAllApplications(): Applications {
        return transaction(db) {
            val competitors = CompetitorsInCompetition.selectAll().map {
                Competitor(
                    it[CompetitorsInCompetition.wishGroup],
                    it[CompetitorsInCompetition.surname],
                    it[CompetitorsInCompetition.name],
                    it[CompetitorsInCompetition.birth],
                    it[CompetitorsInCompetition.title],
                    "", "",
                    it[CompetitorsInCompetition.id]
                )
            }

            val kek = CompetitorsInCompetition.selectAll().groupBy(
                { it[CompetitorsInCompetition.team] }, {
                    competitors.find { competitor -> competitor.id == it[CompetitorsInCompetition.id] }!!
                }
            ).map { (teamName, comps) ->
                Team(teamName, comps)
            }

            Applications(CompetitorsInCompetition
                .selectAll().groupBy(
                    { it[CompetitorsInCompetition.team] }, {
                        competitors.find { competitor -> competitor.id == it[CompetitorsInCompetition.id] }!!
                    }
                ).map { (teamName, comps) ->
                    Team(teamName, comps)
                }
            )
        }
    }

    override fun cleanCompetitors() {
        transaction(db) {
            SchemaUtils.drop(Competitors)
            SchemaUtils.create(Competitors)
        }
    }

    override fun deleteCompetitor(id: Int) {
        transaction(db) {
            Competitors.deleteWhere { Competitors.id eq id }
        }
    }

    override fun setCompetition(competition: Competition) {
        transaction(db) {
            SchemaUtils.drop(CompetitorsInCompetition, Checkpoints)
            SchemaUtils.create(CompetitorsInCompetition, Checkpoints)
            competition.competitors.forEach { competitor ->
                CompetitorsInCompetition.insert {
                    it[id] = competitor.id
                    it[number] = competitor.number
                    it[group] = competitor.group.name
                    it[groupCalculatorId] =
                        when (competitor.group.calculator) {
                            is AllCheckpointsCalculator -> 0
                            is KCheckpointsCalculator -> competitor.group.calculator.minCheckpoints
                            else -> throw Exception("I don't know such calculator :(")
                        }
                    it[checkpointNames] = competitor.group.checkPointNames.joinToString("$")
                    it[wishGroup] = competitor.wishGroup
                    it[surname] = competitor.surname
                    it[team] = competitor.team.name
                    it[name] = competitor.name
                    it[birth] = competitor.birth
                    it[title] = competitor.title
                }
            }
            competition.checkpoints.map { checkPoint ->
                Checkpoints.insert {
                    it[name] = checkPoint.name
                    it[data] = checkPoint.timeMatching.toList().map { (competitor, times) ->
                        competitor.id.toString() + "@@@" + times.joinToString("$$$") { time -> time.time.toString() }
                    }.joinToString("!!!")
                }
            }
        }
    }

    override fun getCompetition(): Competition? {
        return transaction(db) {
            if (CompetitorsInCompetition.selectAll().empty()) {
                return@transaction null
            }
            val groups = CompetitorsInCompetition.selectAll().map {
                Triple(
                    it[CompetitorsInCompetition.group],
                    it[CompetitorsInCompetition.checkpointNames].split("$"),
                    when (it[CompetitorsInCompetition.groupCalculatorId]) {
                        0 -> AllCheckpointsCalculator
                        else -> KCheckpointsCalculator(it[CompetitorsInCompetition.groupCalculatorId])
                    }
                )
            }.distinct().map { Group(it.first, it.second.toMutableList(), it.third) }

            val competitors = CompetitorsInCompetition.selectAll().map {
                Competitor(
                    it[CompetitorsInCompetition.wishGroup],
                    it[CompetitorsInCompetition.surname],
                    it[CompetitorsInCompetition.name],
                    it[CompetitorsInCompetition.birth],
                    it[CompetitorsInCompetition.title],
                    "", "",
                    it[CompetitorsInCompetition.id]
                )
            }


            val teams = CompetitorsInCompetition
                .selectAll().groupBy(
                    { it[CompetitorsInCompetition.team] }, {
                        competitors.find { competitor -> competitor.id == it[CompetitorsInCompetition.id] }!!
                    }
                ).map { (teamName, comps) ->
                    Team(teamName, comps)
                }


            val compInComp = CompetitorsInCompetition.selectAll().map {
                CompetitorInCompetition(
                    competitors.find { competitor -> competitor.id == it[CompetitorsInCompetition.id] }!!,
                    it[CompetitorsInCompetition.number],
                    groups.find { group -> group.name == it[CompetitorsInCompetition.group] }!!,
                    teams.find { team -> team.name == it[CompetitorsInCompetition.team] }!!
                )
            }

            val numberMatching = compInComp.associateBy { competitor -> competitor.number }


            val checkpoints = (Checkpoints).selectAll().map { checkPoint ->
                val data = checkPoint[Checkpoints.data]
                CheckPoint(
                    checkPoint[Checkpoints.name],
                    data.split("!!!").map { it1 ->
                        val tmp1 = it1.split("@@@")
                        val tmp2 = tmp1[1].split("$$$").map { time -> Time(time.toInt()) }.toMutableList()
                        compInComp.find { comp -> comp.id == tmp1[0].toInt() }!! to tmp2
                    }.toMap()
                )
            }.toMutableList()

            Competition(
                checkpoints = checkpoints,
                competitors = compInComp,
                numberMatching = numberMatching
            )
        }
    }

    override fun clearCompetition() {
        transaction(db) {
            SchemaUtils.drop(CompetitorsInCompetition, Checkpoints)
            SchemaUtils.create(CompetitorsInCompetition, Checkpoints)
        }
    }

    override fun setPossibleGroupNames(groups: List<String>) {
        transaction(db) {
            SchemaUtils.drop(PossibleGroups)
            SchemaUtils.create(PossibleGroups)

            groups.forEach { group ->
                PossibleGroups.insert {
                    it[PossibleGroups.group] = group
                }
            }
        }
    }

    override fun getPossibleGroupNames(): List<String> {
        return transaction(db) {
            PossibleGroups.selectAll().map { it[PossibleGroups.group] }
        }
    }
}