package avn.app

import android.content.Context
import androidx.room.Room
import avn.app.core.consent.ConsentManager
import avn.app.core.consent.ConsentScope
import avn.app.data.AlertLogEntity
import avn.app.data.AvnDatabase
import avn.app.data.KnownPersonEntity
import avn.app.data.SettingEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

/** Runs on the JVM through Robolectric, so `./gradlew test` covers it without a phone. */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class AndroidCoreTest {
    private val context: Context = RuntimeEnvironment.getApplication()

    @Test
    fun consentIsStoredWithItsTimestampAndCanBeRevoked() {
        var now = 1_000L
        val prefs = context.getSharedPreferences("consent_test", Context.MODE_PRIVATE)
        val consent = ConsentManager(prefs) { now }

        ConsentScope.entries.forEach { assertFalse(consent.isGranted(it)) }
        assertNull(consent.grantedAt(ConsentScope.CLOUD_FRAMES))

        consent.grant(ConsentScope.CLOUD_FRAMES)
        assertTrue(consent.isGranted(ConsentScope.CLOUD_FRAMES))
        assertEquals(1_000L, consent.grantedAt(ConsentScope.CLOUD_FRAMES))
        assertFalse(consent.isGranted(ConsentScope.CLOUD_SCREEN))
        assertFalse(consent.isGranted(ConsentScope.FACES))

        // A second manager over the same store sees the grant: it survives a restart.
        now = 2_000L
        assertTrue(ConsentManager(prefs) { now }.isGranted(ConsentScope.CLOUD_FRAMES))

        consent.revoke(ConsentScope.CLOUD_FRAMES)
        assertFalse(consent.isGranted(ConsentScope.CLOUD_FRAMES))
        assertNull(consent.grantedAt(ConsentScope.CLOUD_FRAMES))
    }

    @Test
    fun daosRoundTrip() = runBlocking {
        val db = Room.inMemoryDatabaseBuilder(context, AvnDatabase::class.java).allowMainThreadQueries().build()
        try {
            val settings = db.settings()
            assertNull(settings.get("backend_url"))
            settings.put(SettingEntity("backend_url", "http://a"))
            settings.put(SettingEntity("backend_url", "http://b"))
            assertEquals("http://b", settings.get("backend_url"))
            assertEquals("http://b", settings.observe("backend_url").first())

            val log = db.alertLog()
            assertTrue(log.observeRecent(10).first().isEmpty())
            log.insert(AlertLogEntity(0, 1L, "evt_1", "camera", "obstacle_ahead", "chair", "warning", "spoken", null))
            log.insert(
                AlertLogEntity(0, 2L, "evt_2", "camera", "person_close", "person", "info", "suppressed", "cooldown")
            )
            assertEquals(listOf("evt_2", "evt_1"), log.observeRecent(10).first().map { it.eventId })
            assertEquals(1, log.observeRecent(1).first().size)

            val people = db.knownPeople()
            people.upsert(KnownPersonEntity("p1", "Sara", byteArrayOf(1, 2, 3), "mfn-1", 5L))
            assertEquals(listOf("Sara"), people.all().map { it.displayName })
            people.delete("p1")
            assertTrue(people.all().isEmpty())
        } finally {
            db.close()
        }
    }
}
