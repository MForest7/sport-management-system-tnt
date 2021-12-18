package testsForDB

import DB
import org.junit.After
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

class PossibleGroups {

    @Test
    fun check() {
        val db = DB("test")
        val groups = listOf("kek", "lol")
        db.setPossibleGroupNames(groups)
        assertEquals(db.getPossibleGroupNames().toSet(), groups.toSet())
    }

    @After
    fun remove() {
        File("./test.mv.db").delete()
    }
}