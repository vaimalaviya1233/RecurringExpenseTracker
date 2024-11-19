package model.notification

import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.database.UserPreferencesRepository

class PersistentNotificationQueue(
    private val expenseNotificationsPreference: UserPreferencesRepository.Preference<String>,
) : NotificationQueue {
    override suspend fun peek(): NotificationData? {
        val notifications = loadNotifications()
        return notifications.firstOrNull()
    }

    override suspend fun poll(): NotificationData? {
        val notifications = loadNotifications()
        val first =
            notifications.firstOrNull()?.also {
                remove(it)
            }
        return first
    }

    override suspend fun add(notification: NotificationData) {
        loadNotifications().toMutableList().apply {
            add(notification)
            val value = Json.encodeToString(this)
            expenseNotificationsPreference.save(value)
        }
    }

    override suspend fun remove(notification: NotificationData) {
        loadNotifications().toMutableList().apply {
            remove(notification)
            expenseNotificationsPreference.save(Json.encodeToString(this))
        }
    }

    private suspend fun loadNotifications(): List<NotificationData> {
        val notificationsJson = expenseNotificationsPreference.get().first()
        return if (notificationsJson.isEmpty()) {
            emptyList()
        } else {
            Json.decodeFromString(notificationsJson)
        }
    }
}
