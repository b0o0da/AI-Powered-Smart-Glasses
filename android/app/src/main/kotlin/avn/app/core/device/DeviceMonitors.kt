package avn.app.core.device

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioDeviceCallback
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.os.BatteryManager
import androidx.core.content.ContextCompat
import avn.app.core.state.AppEvent
import avn.app.core.state.AppStateStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BatteryMonitor @Inject constructor(
    @ApplicationContext private val context: Context,
    private val store: AppStateStore
) {
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100)
            if (level >= 0 && scale > 0) store.dispatch(AppEvent.Battery(level * 100 / scale))
        }
    }

    fun start() {
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        ContextCompat.registerReceiver(context, receiver, filter, ContextCompat.RECEIVER_NOT_EXPORTED)
    }

    fun stop() {
        runCatching { context.unregisterReceiver(receiver) }
    }
}

private val HEADSET_TYPES = setOf(
    AudioDeviceInfo.TYPE_WIRED_HEADSET,
    AudioDeviceInfo.TYPE_WIRED_HEADPHONES,
    AudioDeviceInfo.TYPE_USB_HEADSET,
    AudioDeviceInfo.TYPE_BLUETOOTH_A2DP,
    AudioDeviceInfo.TYPE_BLUETOOTH_SCO,
    AudioDeviceInfo.TYPE_BLE_HEADSET
)

@Singleton
class HeadsetMonitor @Inject constructor(@ApplicationContext context: Context, private val store: AppStateStore) {
    private val audio = context.getSystemService(AudioManager::class.java)

    private val callback = object : AudioDeviceCallback() {
        override fun onAudioDevicesAdded(addedDevices: Array<out AudioDeviceInfo>) = publish()

        override fun onAudioDevicesRemoved(removedDevices: Array<out AudioDeviceInfo>) = publish()
    }

    fun start() {
        publish()
        audio.registerAudioDeviceCallback(callback, null)
    }

    fun stop() = audio.unregisterAudioDeviceCallback(callback)

    private fun publish() {
        val connected = audio.getDevices(AudioManager.GET_DEVICES_OUTPUTS).any { it.type in HEADSET_TYPES }
        store.dispatch(AppEvent.Headset(connected))
    }
}
