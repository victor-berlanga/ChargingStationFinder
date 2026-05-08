package victor.berlanga.chargingstationfinder.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import victor.berlanga.chargingstationfinder.data.local.entity.FavoriteStationEntity
import victor.berlanga.chargingstationfinder.data.local.entity.StationEntity

@Dao
interface FavoriteStationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favorite: FavoriteStationEntity): Long

    @Query("DELETE FROM favorite_stations WHERE stationId = :stationId")
    suspend fun deleteByStationId(stationId: Int): Int

    @Query("SELECT * FROM favorite_stations ORDER BY savedAt DESC")
    fun getAll(): LiveData<List<FavoriteStationEntity>>

    @Query("SELECT * FROM favorite_stations WHERE stationId = :stationId LIMIT 1")
    suspend fun getByStationId(stationId: Int): FavoriteStationEntity?

    @Query(
        """
        SELECT stations.* FROM stations
        INNER JOIN favorite_stations ON stations.id = favorite_stations.stationId
        ORDER BY favorite_stations.savedAt DESC
        """
    )
    fun getFavoriteStations(): LiveData<List<StationEntity>>
}
