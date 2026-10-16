package avn.app.navigation

import avn.app.core.logging.JsonLinesLogger
import avn.contracts.NavState
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow

/** Route data may use meters; hazards never do. */
data class LocationFix(val lat: Double, val lon: Double, val accuracyM: Float, val timestampMs: Long)

interface LocationSource {
    fun locations(): Flow<LocationFix>

    suspend fun lastKnown(): LocationFix?
}

interface Navigation {
    val state: StateFlow<NavState>

    /** False when no route could be loaded. */
    suspend fun start(destination: String): Boolean

    fun cancel()
}

@Singleton
class FakeLocationSource @Inject constructor(private val log: JsonLinesLogger) : LocationSource {
    override fun locations(): Flow<LocationFix> = emptyFlow()

    override suspend fun lastKnown(): LocationFix? {
        log.log("location", "fake_last_known")
        return null
    }
}

@Singleton
class FakeNavigation @Inject constructor(private val log: JsonLinesLogger) : Navigation {
    override val state: StateFlow<NavState> = MutableStateFlow(NavState())

    override suspend fun start(destination: String): Boolean {
        log.log("navigation", "fake_start")
        return false
    }

    override fun cancel() = log.log("navigation", "fake_cancel")
}
