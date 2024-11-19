package model.notification

import android.app.Notification

interface SystemNotificationBuilder {
    suspend fun buildSystemNotification(data: NotificationData): Notification
}
