package victor.berlanga.chargingstationfinder.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import victor.berlanga.chargingstationfinder.data.CsvStationLoader
import victor.berlanga.chargingstationfinder.data.local.AppDatabase
import victor.berlanga.chargingstationfinder.data.local.entity.FavoriteStationEntity
import victor.berlanga.chargingstationfinder.data.local.entity.ReviewEntity
import victor.berlanga.chargingstationfinder.data.local.entity.SearchHistoryEntity
import victor.berlanga.chargingstationfinder.data.local.entity.StationEntity
import victor.berlanga.chargingstationfinder.data.local.entity.UserEntity

class AppRepository private constructor(private val context: Context) {

    private val database = AppDatabase.getDatabase(context)
    private val userDao = database.userDao()
    private val stationDao = database.stationDao()
    private val favoriteDao = database.favoriteStationDao()
    private val reviewDao = database.reviewDao()
    private val searchHistoryDao = database.searchHistoryDao()

    val stations: LiveData<List<StationEntity>> = stationDao.getAll()
    val favoriteStations: LiveData<List<StationEntity>> = favoriteDao.getFavoriteStations()
    val searchHistory: LiveData<List<SearchHistoryEntity>> = searchHistoryDao.getAll()

    suspend fun seedInitialStationsIfNeeded() {
        withContext(Dispatchers.IO) {
            if (stationDao.countStations() == 0) {
                val stationsFromCsv = CsvStationLoader.loadStations(context)
                stationDao.insertAll(stationsFromCsv)
            }
        }
    }

    fun searchStations(query: String): LiveData<List<StationEntity>> {
        return stationDao.searchStations(query.trim())
    }

    fun filterStations(
        text: String = "",
        connector: String = "",
        municipality: String = "",
        usageType: String = "",
        currentType: String = ""
    ): LiveData<List<StationEntity>> {
        return stationDao.filterStations(
            text.trim(),
            connector.trim(),
            municipality.trim(),
            usageType.trim(),
            currentType.trim()
        )
    }

    suspend fun getStationById(id: Int): StationEntity? {
        return withContext(Dispatchers.IO) {
            stationDao.getById(id)
        }
    }

    suspend fun saveFavorite(stationId: Int) {
        withContext(Dispatchers.IO) {
            favoriteDao.insert(FavoriteStationEntity(stationId = stationId))
        }
    }

    suspend fun removeFavorite(stationId: Int) {
        withContext(Dispatchers.IO) {
            favoriteDao.deleteByStationId(stationId)
        }
    }

    suspend fun isFavorite(stationId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            favoriteDao.getByStationId(stationId) != null
        }
    }

    fun getReviewsByStationId(stationId: Int): LiveData<List<ReviewEntity>> {
        return reviewDao.getReviewsByStationId(stationId)
    }

    suspend fun addReview(stationId: Int, comment: String, rating: Int) {
        withContext(Dispatchers.IO) {
            reviewDao.insert(
                ReviewEntity(
                    stationId = stationId,
                    comment = comment.trim(),
                    rating = rating
                )
            )
        }
    }

    suspend fun saveUserRegistration(name: String, email: String) {
        withContext(Dispatchers.IO) {
            userDao.insert(UserEntity(name = name.trim(), email = email.trim()))
        }
    }

    suspend fun saveSearchHistory(query: String) {
        val cleanQuery = query.trim()
        if (cleanQuery.isEmpty()) return

        withContext(Dispatchers.IO) {
            searchHistoryDao.insert(SearchHistoryEntity(query = cleanQuery))
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AppRepository? = null

        fun getInstance(context: Context): AppRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = AppRepository(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }
}
