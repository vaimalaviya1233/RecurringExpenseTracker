package model.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import org.jetbrains.compose.resources.getString

@RequiresApi(Build.VERSION_CODES.O)
class SystemNotificationBuilderApi26(
    private val delegate: SystemNotificationBuilder,
    private val notificationManager: NotificationManager,
) : SystemNotificationBuilder {
    override suspend fun buildSystemNotification(data: NotificationData): Notification {
        val channel = createChannel(data.channel)
        notificationManager.createNotificationChannel(channel)

        return delegate.buildSystemNotification(data)
    }

    private suspend fun createChannel(channel: model.notification.NotificationChannel): NotificationChannel =
        NotificationChannel(
            channel.id,
            getString(channel.displayNameRes),
            NotificationManager.IMPORTANCE_DEFAULT,
        )
}
