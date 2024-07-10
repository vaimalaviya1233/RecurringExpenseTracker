package di

import Constants
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.RoomDatabase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import viewmodel.database.RecurringExpenseDatabase
import viewmodel.database.UserPreferencesRepository
import viewmodel.database.getDatabaseBuilder

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
    }
