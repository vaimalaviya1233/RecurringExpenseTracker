package model.notification

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.CATEGORY_ALARM
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import de.dbauer.expensetracker.MainActivity
import de.dbauer.expensetracker.R

class SystemNotificationBuilderDefault(
    private val context: Context,
) : SystemNotificationBuilder {
    override suspend fun buildSystemNotification(data: NotificationData): Notification {
//        val remoteViews = setupRemoteViews(data)

        val launchIntent =
            PendingIntent.getActivity(
                context,
                data.id,
                Intent(context, MainActivity::class.java),
                PendingIntent.FLAG_IMMUTABLE,
            )
        val dismissIntent = PendingIntent.getBroadcast(context, data.id, Intent(), PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat
            .Builder(context, data.channel.id)
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setAutoCancel(true)
            .setContentText(data.description)
            .setContentTitle(data.title)
            .setPriority(PRIORITY_MAX)
            .setCategory(CATEGORY_ALARM)
            .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE)
            .setContentIntent(launchIntent)
            .setDeleteIntent(dismissIntent)
//            .setCustomContentView(remoteViews)
            .build()
    }

    private fun setupRemoteViews(data: NotificationData): RemoteViews {
        TODO("Not yet implemented")
    }
}
