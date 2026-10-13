package avn.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import avn.app.core.connectivity.ConnectivityMonitor
import avn.app.core.consent.ConsentManager
import avn.app.core.permissions.PermissionManager
import avn.app.core.state.AppStateStore
import avn.app.data.AlertLogDao
import avn.app.data.SettingsDao
import avn.app.voice.FakeSpeaker
import avn.app.voice.FakeSttEngine
import avn.app.voice.FakeWakeWord
import avn.contracts.EventType
import avn.contracts.Source
import avn.contracts.SpeechCandidate
import avn.engine.EngineInput
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/** What the four screens need. The Debug screen drives the fakes directly, so it names them. */
class UiDeps @Inject constructor(
    val store: AppStateStore,
    val permissions: PermissionManager,
    val consent: ConsentManager,
    val settings: SettingsDao,
    val alertLog: AlertLogDao,
    val engine: EngineInput,
    val connectivity: ConnectivityMonitor,
    val wakeWord: FakeWakeWord,
    val stt: FakeSttEngine,
    val speaker: FakeSpeaker
) {
    /** The UI never speaks; it asks the engine like every other producer. */
    fun say(text: String) =
        engine.submit(SpeechCandidate(text, Source.SYSTEM, EventType.SYSTEM_NOTICE, userRequested = true))
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var deps: UiDeps

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { AvnScreens(deps) } }
    }
}
