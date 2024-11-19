package model.notification

import kotlin.reflect.KClass

class TestNotificationSchedule

class AppModule {
    fun notificationMetadataProvider(): NotificationMetadataProvider =
        object : NotificationMetadataProvider {
            private val sourcesToIds =
                mapOf(
                    TestNotificationSchedule::class to 1122,
                )

            override fun getIdFor(source: KClass<*>): Int =
                sourcesToIds[source] ?: throw IllegalArgumentException()
        }
}
