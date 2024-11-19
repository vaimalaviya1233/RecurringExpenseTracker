package di

import Constants
import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.RoomDatabase
import model.database.RecurringExpenseDatabase
import model.database.UserPreferencesRepository
import model.database.getDatabaseBuilder
import model.notification.NotificationQueue
import model.notification.PersistentNotificationQueue
import model.notification.SystemNotificationBuilder
import model.notification.SystemNotificationBuilderFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule =
    module {
        singleOf(::getDatabaseBuilder).bind<RoomDatabase.Builder<RecurringExpenseDatabase>>()
        single<DataStore<Preferences>> {
            val context = get<Context>()
            PreferenceDataStoreFactory.create {
                context.preferencesDataStoreFile(name = Constants.USER_PREFERENCES_DATA_STORE)
            }
        }
        singleOf(::UserPreferencesRepository)
        singleOf(::PersistentNotificationQueue).bind(NotificationQueue::class)
        single<NotificationQueue> {
            val userPreferencesRepository = get<UserPreferencesRepository>()
            PersistentNotificationQueue(userPreferencesRepository.expenseNotifications)
        }
        factory<AlarmManager> {
            val context = get<Context>()
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        }
        factory<NotificationManager> {
            val context = get<Context>()
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
        factory<SystemNotificationBuilder> {
            SystemNotificationBuilderFactory(get<Context>(), get<NotificationManager>()).create()
        }
    }
