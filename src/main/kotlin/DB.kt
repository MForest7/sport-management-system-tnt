import classes.Competition
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.lang.Exception

interface CompetitorsDB {
    /**
     * data keys format:
     * id, wishGroup, surname, team, name, birth, title
     */
    fun updateCompetitor(id: Int, data: Map<String, String>)
    fun createEmptyCompetitor(): Int
    fun getAllCompetitors(columns: List<String>): List<List<String>>
    fun cleanCompetitors()
    fun deleteCompetitor(id: Int)
}

interface SortitionDB {
    fun setCompetition(competition: Competition)
    fun getCompetition(): Competition
}

class DB(name: String) : CompetitorsDB, SortitionDB {
    companion object {
        const val idPropertyName = "id"
        const val wishGroupPropertyName = "wishGroup"
        const val surnamePropertyName = "surname"
        const val namePropertyName = "name"
        const val titlePropertyName = "title"
        const val birthPropertyName = "birth"
        const val teamPropertyName = "team"
    }

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
        val id = integer("id").uniqueIndex().references(Competitors.id)
        val number = text("number")
        val group = text("group")
        val startTime = integer("startTime")
    }

    private val db = Database.connect("jdbc:h2:./$name;DB_CLOSE_DELAY=-1;", "org.h2.Driver")

    init {
        transaction(db) {
            SchemaUtils.create(Competitors)
        }
    }

    override fun updateCompetitor(id: Int, data: Map<String, String>) {
        transaction {
            data.forEach { (property, value) ->
                Competitors.update({ Competitors.id eq id }) {
                    when (property) {
                        wishGroupPropertyName -> it[wishGroup] = value
                        surnamePropertyName -> it[surname] = value
                        namePropertyName -> it[name] = value
                        titlePropertyName -> it[title] = value
                        birthPropertyName -> it[birth] = value
                        teamPropertyName -> it[team] = value
                        else -> throw Exception("Unknown property!")
                    }
                }
            }
        }
    }


    override fun createEmptyCompetitor(): Int {
        return transaction(db) {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(Competitors)
            Competitors.insert {
                it[wishGroup] = null
                it[surname] = null
                it[name] = null
                it[title] = null
                it[birth] = null
                it[team] = null
            } get Competitors.id
        }
    }

    override fun getAllCompetitors(columns: List<String>): List<List<String>> {
        return transaction(db) {
            Competitors.selectAll().map {
                columns.map { column ->
                    when (column) {
                        idPropertyName -> it[Competitors.id].toString()
                        wishGroupPropertyName -> it[Competitors.wishGroup] ?: ""
                        surnamePropertyName -> it[Competitors.surname] ?: ""
                        namePropertyName -> it[Competitors.name] ?: ""
                        titlePropertyName -> it[Competitors.title] ?: ""
                        birthPropertyName -> it[Competitors.birth] ?: ""
                        teamPropertyName -> it[Competitors.team] ?: ""
                        else -> throw Exception("Unknown property!")
                    }
                }
            }
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
            competition.competitors.forEach { competitor ->
                CompetitorsInCompetition.update({ CompetitorsInCompetition.id eq 8 }) {
                    it[startTime] = competition.start.timeMatching[competitor]?.first()?.time
                        ?: throw Exception("I don't know what happened :( Everything must be ok!")
                }
            }
        }
    }

    override fun getCompetition(): Competition {
        TODO("Not yet implemented")
    }
}