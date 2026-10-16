package avn.app.screen

import android.graphics.Bitmap
import avn.app.core.logging.JsonLinesLogger
import avn.contracts.ScreenFlags
import avn.contracts.ScreenSnapshot
import avn.contracts.ScreenType
import javax.inject.Inject
import javax.inject.Singleton

enum class ScrollDirection { FORWARD, BACKWARD }

enum class SummarySource { TREE, OCR, VLM }

data class ScreenSummary(val text: String, val source: SummarySource)

/** In memory only, never persisted. */
data class AppNotification(val packageName: String, val sender: String, val text: String, val timestampMs: Long)

interface ScreenReader {
    /** A blocked screen (password, secure window, deny-listed package) returns no nodes and no text. */
    suspend fun snapshot(): ScreenSnapshot
}

/** Every action is announced through the engine before it runs. Each returns false when it could not run. */
interface ScreenActions {
    suspend fun scroll(direction: ScrollDirection): Boolean

    suspend fun back(): Boolean

    suspend fun tap(label: String): Boolean
}

/** TD-004 picks the default implementation. Null when the screen is secure, blank or consent is missing. */
interface ScreenCapture {
    suspend fun capture(): Bitmap?
}

interface ScreenAi {
    suspend fun summarize(snapshot: ScreenSnapshot, question: String): ScreenSummary
}

/** Read-only. There is no reply or write path. */
interface NotificationSource {
    fun pending(): List<AppNotification>

    fun markRead()
}

@Singleton
class FakeScreenReader @Inject constructor(private val log: JsonLinesLogger) : ScreenReader {
    override suspend fun snapshot(): ScreenSnapshot {
        log.log("screen", "fake_snapshot")
        return ScreenSnapshot("fake", ScreenType.OTHER, emptyList(), "", emptyList(), ScreenFlags(empty = true))
    }
}

@Singleton
class FakeScreenActions @Inject constructor(private val log: JsonLinesLogger) : ScreenActions {
    override suspend fun scroll(direction: ScrollDirection): Boolean = fake("scroll")

    override suspend fun back(): Boolean = fake("back")

    override suspend fun tap(label: String): Boolean = fake("tap")

    private fun fake(action: String): Boolean {
        log.log("screen", "fake_action", fields = mapOf("action" to action))
        return false
    }
}

@Singleton
class FakeScreenCapture @Inject constructor(private val log: JsonLinesLogger) : ScreenCapture {
    override suspend fun capture(): Bitmap? {
        log.log("screen", "fake_capture")
        return null
    }
}

@Singleton
class FakeScreenAi @Inject constructor(private val log: JsonLinesLogger) : ScreenAi {
    override suspend fun summarize(snapshot: ScreenSnapshot, question: String): ScreenSummary {
        log.log("screen", "fake_summarize")
        return ScreenSummary(snapshot.text, SummarySource.TREE)
    }
}

@Singleton
class FakeNotificationSource @Inject constructor(private val log: JsonLinesLogger) : NotificationSource {
    override fun pending(): List<AppNotification> {
        log.log("notifications", "fake_pending")
        return emptyList()
    }

    override fun markRead() = log.log("notifications", "fake_mark_read")
}
