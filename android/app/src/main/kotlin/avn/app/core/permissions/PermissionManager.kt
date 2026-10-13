package avn.app.core.permissions

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import avn.app.screen.accessibility.AssistantAccessibilityService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/** Everything onboarding must obtain. [permission] is null for special access granted in system settings. */
enum class Access(val permission: String?, val label: String, val deniedSpeech: String) {
    CAMERA(
        Manifest.permission.CAMERA,
        "Camera",
        "Without the camera I can't warn you about obstacles or read text."
    ),
    MICROPHONE(
        Manifest.permission.RECORD_AUDIO,
        "Microphone",
        "Without the microphone I can't hear your commands."
    ),
    LOCATION(
        Manifest.permission.ACCESS_FINE_LOCATION,
        "Precise location",
        "Without location I can't guide you or add your position to an S O S message."
    ),
    SMS(
        Manifest.permission.SEND_SMS,
        "Send SMS",
        "Without SMS permission I can't send an S O S message."
    ),
    NOTIFICATIONS(
        "android.permission.POST_NOTIFICATIONS",
        "Notifications",
        "Without notification permission the assistant may be stopped in the background."
    ),
    ACCESSIBILITY(
        null,
        "Accessibility service",
        "Without the accessibility service I can't read your screen."
    ),
    NOTIFICATION_LISTENER(
        null,
        "Notification access",
        "Without notification access I can't read your new messages."
    ),
    BATTERY(
        null,
        "Unrestricted battery",
        "With battery optimisation on, the assistant may stop when the screen is off."
    )
}

@Singleton
class PermissionManager @Inject constructor(@ApplicationContext private val context: Context) {

    fun isGranted(access: Access): Boolean = when (access) {
        Access.NOTIFICATIONS -> Build.VERSION.SDK_INT < 33 || hasPermission(access.permission!!)
        Access.ACCESSIBILITY -> isAccessibilityEnabled()
        Access.NOTIFICATION_LISTENER ->
            NotificationManagerCompat.getEnabledListenerPackages(context).contains(context.packageName)
        Access.BATTERY ->
            context.getSystemService(PowerManager::class.java).isIgnoringBatteryOptimizations(context.packageName)
        else -> hasPermission(access.permission!!)
    }

    fun missing(): List<Access> = Access.entries.filterNot(::isGranted)

    /** Settings screen for a special access; the user (or a sighted helper) turns it on by hand. */
    fun settingsIntent(access: Access): Intent = when (access) {
        Access.ACCESSIBILITY -> Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        Access.NOTIFICATION_LISTENER -> Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
        Access.BATTERY -> Intent(
            Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
            Uri.parse("package:${context.packageName}")
        )
        else -> Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:${context.packageName}"))
    }.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    private fun hasPermission(permission: String) =
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    private fun isAccessibilityEnabled(): Boolean {
        val enabled = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false
        val mine = ComponentName(context, AssistantAccessibilityService::class.java)
        return enabled.split(':').any { ComponentName.unflattenFromString(it) == mine }
    }
}
