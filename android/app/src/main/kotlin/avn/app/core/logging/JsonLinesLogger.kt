package avn.app.core.logging

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.util.UUID
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

private const val MAX_FILE_BYTES = 2_000_000L
private const val MAX_VALUE_CHARS = 80

// ponytail: content-free policy is a key deny-list plus a length cap, not content inspection.
// Upgrade to an allow-list of field names per module if a review finds content slipping through.
private val CONTENT_KEYS = setOf(
    "text", "transcript", "image", "screenshot", "embedding", "question", "answer",
    "body", "message", "sender", "title", "summary", "description"
)

internal fun formatLine(ts: Long, module: String, event: String, traceId: String, fields: Map<String, Any?>): String =
    buildJsonObject {
        put("ts", ts)
        put("module", module)
        put("event", event)
        put("trace", traceId)
        for ((key, value) in fields) {
            when {
                key.lowercase() in CONTENT_KEYS -> put(key, "[redacted]")
                value == null -> put(key, JsonNull)
                value is Number -> put(key, value)
                value is Boolean -> put(key, value)
                else -> put(key, value.toString().take(MAX_VALUE_CHARS))
            }
        }
    }.toString()

/** JSON lines, one trace ID per request or event chain. Never pass frames, screen text or transcripts. */
@Singleton
class JsonLinesLogger @Inject constructor(@ApplicationContext context: Context) {
    private val file = File(context.filesDir, "logs/avn.jsonl")
    private val io = Executors.newSingleThreadExecutor()

    fun newTraceId(): String = UUID.randomUUID().toString().take(8)

    fun log(module: String, event: String, traceId: String = newTraceId(), fields: Map<String, Any?> = emptyMap()) {
        val line = formatLine(System.currentTimeMillis(), module, event, traceId, fields)
        Log.d("AVN", line)
        io.execute {
            runCatching {
                file.parentFile?.mkdirs()
                if (file.length() > MAX_FILE_BYTES) file.renameTo(File(file.path + ".1"))
                file.appendText(line + "\n")
            }
        }
    }
}
