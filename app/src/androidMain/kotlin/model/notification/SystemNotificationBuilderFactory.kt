package model.notification

import android.app.NotificationManager
import android.content.Context
import android.os.Build

class SystemNotificationBuilderFactory(
    private val context: Context,
    private val notificationManager: NotificationManager,
) {
    fun create(): SystemNotificationBuilder {
        val defaultSystemNotificationManager = SystemNotificationBuilderDefault(context)

        return if (Build.VERSION.SDK_INT >= 26) {
            SystemNotificationBuilderApi26(
                defaultSystemNotificationManager,
                notificationManager,
            )
        } else {
            defaultSystemNotificationManager
        }
    }
}
