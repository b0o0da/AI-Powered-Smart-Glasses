package avn.app.screen.notifications

import android.service.notification.NotificationListenerService

/** Declared so notification access can be granted during onboarding. Reading arrives in Phase 7; never replies. */
class AssistantNotificationListener : NotificationListenerService()
