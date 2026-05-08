package victor.berlanga.chargingstationfinder.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import victor.berlanga.chargingstationfinder.data.local.entity.StationEntity

@Dao
interface StationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stations: List<StationEntity>)

    @Query("SELECT COUNT(*) FROM stations")
    suspend fun countStations(): Int

    @Query("SELECT * FROM stations ORDER BY municipality, name")
    fun getAll(): LiveData<List<StationEntity>>

    @Query("SELECT * FROM stations WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): StationEntity?

    @Query(
        """
        SELECT * FROM stations
        WHERE name LIKE '%' || :text || '%'
           OR address LIKE '%' || :text || '%'
           OR municipality LIKE '%' || :text || '%'
           OR connectorStandard LIKE '%' || :text || '%'
        ORDER BY municipality, name
        """
    )
    fun searchStations(text: String): LiveData<List<StationEntity>>

    @Query(
        """
        SELECT * FROM stations
        WHERE (:text = ''
           OR name LIKE '%' || :text || '%'
           OR address LIKE '%' || :text || '%'
           OR municipality LIKE '%' || :text || '%'
           OR connectorStandard LIKE '%' || :text || '%')
          AND (:connector = '' OR connectorStandard LIKE '%' || :connector || '%')
          AND (:municipality = '' OR municipality LIKE '%' || :municipality || '%')
          AND (:usageType = '' OR usageType LIKE '%' || :usageType || '%')
          AND (:currentType = '' OR currentType LIKE '%' || :currentType || '%')
        ORDER BY municipality, name
        """
    )
    fun filterStations(
        text: String,
        connector: String,
        municipality: String,
        usageType: String,
        currentType: String
    ): LiveData<List<StationEntity>>
}
