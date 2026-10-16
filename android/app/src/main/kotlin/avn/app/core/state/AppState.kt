package avn.app.core.state

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class Health { OK, DEGRADED, DOWN }

data class AppState(
    val listening: Boolean = false,
    val cameraOn: Boolean = false,
    val offline: Boolean = false,
    val battery: Int = 100,
    val headsetConnected: Boolean = false,
    val navigationActive: Boolean = false,
    val sosActive: Boolean = false,
    val moduleHealth: Map<String, Health> = emptyMap()
)

sealed interface AppEvent {
    data class Listening(val on: Boolean) : AppEvent

    data class CameraOn(val on: Boolean) : AppEvent

    data class Offline(val offline: Boolean) : AppEvent

    data class Battery(val percent: Int) : AppEvent

    data class Headset(val connected: Boolean) : AppEvent

    data class NavigationActive(val active: Boolean) : AppEvent

    data class SosActive(val active: Boolean) : AppEvent

    data class ModuleHealth(val module: String, val health: Health) : AppEvent
}

fun reduce(state: AppState, event: AppEvent): AppState = when (event) {
    is AppEvent.Listening -> state.copy(listening = event.on)
    is AppEvent.CameraOn -> state.copy(cameraOn = event.on)
    is AppEvent.Offline -> state.copy(offline = event.offline)
    is AppEvent.Battery -> state.copy(battery = event.percent.coerceIn(0, 100))
    is AppEvent.Headset -> state.copy(headsetConnected = event.connected)
    is AppEvent.NavigationActive -> state.copy(navigationActive = event.active)
    is AppEvent.SosActive -> state.copy(sosActive = event.active)
    is AppEvent.ModuleHealth -> state.copy(moduleHealth = state.moduleHealth + (event.module to event.health))
}

@Singleton
class AppStateStore @Inject constructor() {
    private val mutable = MutableStateFlow(AppState())
    val state: StateFlow<AppState> = mutable.asStateFlow()

    fun dispatch(event: AppEvent) = mutable.update { reduce(it, event) }
}
