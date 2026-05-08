package victor.berlanga.chargingstationfinder.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import victor.berlanga.chargingstationfinder.data.local.dao.FavoriteStationDao
import victor.berlanga.chargingstationfinder.data.local.dao.ReviewDao
import victor.berlanga.chargingstationfinder.data.local.dao.SearchHistoryDao
import victor.berlanga.chargingstationfinder.data.local.dao.StationDao
import victor.berlanga.chargingstationfinder.data.local.dao.UserDao
import victor.berlanga.chargingstationfinder.data.local.entity.FavoriteStationEntity
import victor.berlanga.chargingstationfinder.data.local.entity.ReviewEntity
import victor.berlanga.chargingstationfinder.data.local.entity.SearchHistoryEntity
import victor.berlanga.chargingstationfinder.data.local.entity.StationEntity
import victor.berlanga.chargingstationfinder.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        StationEntity::class,
        FavoriteStationEntity::class,
        ReviewEntity::class,
        SearchHistoryEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun stationDao(): StationDao
    abstract fun favoriteStationDao(): FavoriteStationDao
    abstract fun reviewDao(): ReviewDao
    abstract fun searchHistoryDao(): SearchHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "charging_station_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
