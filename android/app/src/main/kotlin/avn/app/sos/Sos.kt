package avn.app.sos

import avn.app.core.logging.JsonLinesLogger
import javax.inject.Inject
import javax.inject.Singleton

enum class SosTrigger { KEYWORD, INTENT, BUTTON }

/** SMS goes only to contacts from settings. The emergency-number path does not exist in any build. */
interface Sos {
    fun trigger(source: SosTrigger)

    fun cancel()
}

@Singleton
class FakeSos @Inject constructor(private val log: JsonLinesLogger) : Sos {
    override fun trigger(source: SosTrigger) = log.log("sos", "fake_trigger", fields = mapOf("source" to source))

    override fun cancel() = log.log("sos", "fake_cancel")
}
