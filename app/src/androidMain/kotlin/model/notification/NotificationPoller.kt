package model.notification

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.koin.java.KoinJavaComponent.inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

internal const val ACTION_NOTIFICATION_POLL = "notification_poll"

internal class NotificationPoller : AlarmLoopReceiver() {
    private val queue: NotificationQueue by inject(NotificationQueue::class.java)

    override val alarmManager: AlarmManager by inject(AlarmManager::class.java)

    private val systemNotificationBuilder: SystemNotificationBuilder by inject(
        SystemNotificationBuilder::class.java,
    )

    private val notificationManager: NotificationManager by inject(NotificationManager::class.java)

    override val loopPeriod: Duration
        get() = 1.minutes

    override val loopAction: String
        get() = ACTION_NOTIFICATION_POLL

    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        super.onReceive(context, intent)
        if (intent.action != ACTION_NOTIFICATION_POLL) return

        runBlocking(Dispatchers.IO) {
            val data = queue.poll()
            if (data != null) {
                showNotification(data)
                loop(context)
            } else {
                stop(context)
            }
        }
    }

    private suspend fun showNotification(data: NotificationData) {
        val notification = systemNotificationBuilder.buildSystemNotification(data)
        notificationManager.notify(data.id, notification)
    }
}
