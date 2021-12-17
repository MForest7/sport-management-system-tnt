package testsForDB

import DB
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

class TestsCompetitors {

    companion object {
        var counter = 1
    }

    lateinit var db: DB

    @Before
    fun init() {
        db = DB("./test$counter")
    }

    @After
    fun remove() {
        File("./test$counter.mv.db").delete()
        counter++
    }

    @Test
    fun testCreation() {
        for (i in 1..10) {
            val id = db.createEmptyCompetitor()
            db.updateCompetitor(
                id, mapOf(
                    DB.wishGroupPropertyName to "M$i",
                    DB.teamPropertyName to "KEKTeam"
                )
            )
            val ask = db.getAllCompetitors(
                listOf(DB.teamPropertyName, DB.wishGroupPropertyName, DB.birthPropertyName)
            )
            assertEquals(ask.size, i)
            assertEquals(ask[i - 1], listOf("KEKTeam", "M$i", ""))
        }
    }

    @Test
    fun testAccess() {
        for (i in 1..10) {
            val id = db.createEmptyCompetitor()
            db.updateCompetitor(
                id, mapOf(
                    DB.wishGroupPropertyName to "M$i",
                    DB.teamPropertyName to "KEKTeam"
                )
            )
            val ask = db.getAllCompetitors(
                listOf(DB.teamPropertyName, DB.wishGroupPropertyName, DB.birthPropertyName)
            )
            assertEquals(ask.size, i)
            assertEquals(ask[i - 1], listOf("KEKTeam", "M$i", ""))
        }
        val list = mutableSetOf<List<String>>()
        for (i in (5 downTo 1) + (6..10)) {
            db.updateCompetitor(i, mapOf(DB.surnamePropertyName to "Ivanov$i"))
            list.add(listOf("Ivanov$i"))
            val cur = db.getAllCompetitors(listOf(DB.surnamePropertyName)).filter { it != listOf("") }.toSet()
            assertEquals(
                list,
                cur
            )
        }
    }
}