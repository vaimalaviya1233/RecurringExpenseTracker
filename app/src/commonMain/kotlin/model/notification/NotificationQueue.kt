package model.notification

interface NotificationQueue {
    suspend fun add(notification: NotificationData)

    suspend fun remove(notification: NotificationData)

    suspend fun poll(): NotificationData?

    suspend fun peek(): NotificationData?
}
