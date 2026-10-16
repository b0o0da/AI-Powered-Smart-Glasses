package avn.app.core

import avn.app.core.logging.JsonLinesLogger
import avn.contracts.Event
import avn.contracts.Level
import avn.contracts.SpeechCandidate
import avn.contracts.SpeechRequest
import avn.engine.EngineInput
import avn.engine.SpeechOutput
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Stand-in until the real engine lands in Phase 3: logs events and passes every candidate straight to
 * the speaker as Info. No risk scoring, thresholds, cooldowns or queue.
 */
@Singleton
class FakeEngineInput @Inject constructor(private val speaker: SpeechOutput, private val log: JsonLinesLogger) :
    EngineInput {
    private val counter = AtomicLong()

    override fun submit(event: Event) {
        val fields = mapOf(
            "id" to event.id,
            "source" to event.source,
            "type" to event.type,
            "label" to event.label,
            "side" to event.side,
            "band" to event.distanceBand,
            "confidence" to event.confidence
        )
        log.log("engine", "fake_event", fields = fields)
    }

    override fun submit(candidate: SpeechCandidate) {
        val request = SpeechRequest(
            utteranceId = "utt_${counter.incrementAndGet()}",
            text = candidate.text,
            level = Level.INFO,
            source = candidate.source,
            interruptible = true,
            eventId = candidate.eventId
        )
        speaker.speak(request)
    }
}
