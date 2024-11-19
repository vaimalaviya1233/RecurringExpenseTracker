package model.notification

import kotlin.reflect.KClass

interface NotificationMetadataProvider {
    fun getIdFor(source: KClass<*>): Int
}
