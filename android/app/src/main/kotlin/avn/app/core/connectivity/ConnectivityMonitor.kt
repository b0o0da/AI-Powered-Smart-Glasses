package avn.app.core.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import avn.app.core.state.AppEvent
import avn.app.core.state.AppStateStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/** Network state -> AppState.offline. Phase 2 adds /health polling to the same flag. */
@Singleton
class ConnectivityMonitor @Inject constructor(@ApplicationContext context: Context, private val store: AppStateStore) {
    private val manager = context.getSystemService(ConnectivityManager::class.java)

    @Volatile
    private var networkUp = false

    @Volatile
    var forcedOffline = false
        private set

    private val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            networkUp = true
            publish()
        }

        override fun onLost(network: Network) {
            networkUp = false
            publish()
        }
    }

    fun start() {
        networkUp = manager.activeNetwork != null
        publish()
        manager.registerDefaultNetworkCallback(callback)
    }

    fun stop() {
        runCatching { manager.unregisterNetworkCallback(callback) }
    }

    /** Debug screen toggle. */
    fun forceOffline(on: Boolean) {
        forcedOffline = on
        publish()
    }

    private fun publish() = store.dispatch(AppEvent.Offline(forcedOffline || !networkUp))
}
