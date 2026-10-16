package avn.app.core.consent

import android.content.SharedPreferences

enum class ConsentScope(val label: String) {
    CLOUD_FRAMES("Send camera pictures to the cloud for descriptions"),
    CLOUD_SCREEN("Send screen content to the cloud for summaries"),
    FACES("Recognise people who agreed to be enrolled")
}

/** Every cloud call checks [isGranted] first. A grant is stored as its timestamp. */
class ConsentManager(private val prefs: SharedPreferences, private val now: () -> Long = System::currentTimeMillis) {
    fun grant(scope: ConsentScope) = prefs.edit().putLong(scope.name, now()).apply()

    fun revoke(scope: ConsentScope) = prefs.edit().remove(scope.name).apply()

    fun isGranted(scope: ConsentScope): Boolean = prefs.contains(scope.name)

    fun grantedAt(scope: ConsentScope): Long? = if (isGranted(scope)) prefs.getLong(scope.name, 0) else null
}
