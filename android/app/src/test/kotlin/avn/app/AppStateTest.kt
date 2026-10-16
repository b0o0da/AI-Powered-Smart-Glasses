package avn.app

import avn.app.core.logging.formatLine
import avn.app.core.state.AppEvent
import avn.app.core.state.AppState
import avn.app.core.state.Health
import avn.app.core.state.reduce
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AppStateTest {
    @Test
    fun eachEventChangesOnlyItsField() {
        val start = AppState()
        assertEquals(start.copy(listening = true), reduce(start, AppEvent.Listening(true)))
        assertEquals(start.copy(cameraOn = true), reduce(start, AppEvent.CameraOn(true)))
        assertEquals(start.copy(offline = true), reduce(start, AppEvent.Offline(true)))
        assertEquals(start.copy(headsetConnected = true), reduce(start, AppEvent.Headset(true)))
        assertEquals(start.copy(navigationActive = true), reduce(start, AppEvent.NavigationActive(true)))
        assertEquals(start.copy(sosActive = true), reduce(start, AppEvent.SosActive(true)))
    }

    @Test
    fun batteryIsClampedToPercent() {
        assertEquals(0, reduce(AppState(), AppEvent.Battery(-5)).battery)
        assertEquals(100, reduce(AppState(), AppEvent.Battery(140)).battery)
        assertEquals(37, reduce(AppState(), AppEvent.Battery(37)).battery)
    }

    @Test
    fun moduleHealthKeepsOtherModules() {
        val one = reduce(AppState(), AppEvent.ModuleHealth("camera", Health.DOWN))
        val two = reduce(one, AppEvent.ModuleHealth("stt", Health.DEGRADED))
        val three = reduce(two, AppEvent.ModuleHealth("camera", Health.OK))
        assertEquals(mapOf("camera" to Health.OK, "stt" to Health.DEGRADED), three.moduleHealth)
    }

    @Test
    fun logLineRedactsContentAndCapsLength() {
        val fields = mapOf("text" to "my private message", "label" to "x".repeat(500), "count" to 3, "ok" to true)
        val line = formatLine(1L, "screen", "read", "abc", fields)
        assertFalse(line.contains("private"))
        assertTrue(line.contains("\"text\":\"[redacted]\""))
        assertTrue(line.contains("\"count\":3"))
        assertTrue(line.contains("\"trace\":\"abc\""))
        assertTrue(line.length < 300)
    }
}
