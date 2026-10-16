package avn.contracts

import java.io.File
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** Phase 1 covers the fixtures whose models exist; Phase 2 extends this to every file in index.json. */
class FixtureTest {
    private val fixtures = File(System.getProperty("avn.repoRoot"), "contracts/fixtures")

    @Test
    fun eventFixturesRoundTrip() {
        val files = File(fixtures, "events").listFiles { f -> f.extension == "json" }.orEmpty()
        assertTrue("no event fixtures found in $fixtures", files.isNotEmpty())
        for (file in files) {
            val event = Json.decodeFromString(Event.serializer(), file.readText())
            val again = Json.decodeFromString(Event.serializer(), Json.encodeToString(Event.serializer(), event))
            assertEquals(file.name, event, again)
        }
    }

    @Test
    fun screenFixturesParse() {
        val files = File(fixtures, "screens").listFiles { f -> f.extension == "json" }.orEmpty()
        assertTrue("no screen fixtures found in $fixtures", files.isNotEmpty())
        files.forEach { Json.decodeFromString(ScreenSnapshot.serializer(), it.readText()) }
    }
}
