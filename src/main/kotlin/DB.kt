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
}



class DB(name: String) : CompetitorsDB {
    companion object {
        const val idPropertyName = "id"
        const val wishGroupPropertyName = "wishGroup"
        const val surnamePropertyName = "surname"
        const val namePropertyName = "name"
        const val titlePropertyName = "title"
        const val birthPropertyName = "birth"
        const val teamPropertyName = "team"
    }

    private object Competitors : Table("Applications") {
        val id = integer("id").autoIncrement().primaryKey()
        val wishGroup = varchar("wishGroup", 20).nullable()
        val surname = varchar("surname", 20).nullable()
        val team = varchar("team", 20).nullable()
        val name = varchar("name", 20).nullable()
        val birth = varchar("birth", 20).nullable()
        val title = varchar("title", 20).nullable()
    }


    private val db = Database.connect("jdbc:h2:./$name", driver = "org.h2.Driver")

    init {
        transaction(db) {
            SchemaUtils.create(Competitors)
        }
    }

    override fun updateCompetitor(id: Int, data: Map<String, String>) {
        transaction(db) {
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
}