package victor.berlanga.chargingstationfinder.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import victor.berlanga.chargingstationfinder.data.local.entity.SearchHistoryEntity

@Dao
interface SearchHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(search: SearchHistoryEntity): Long

    @Query("SELECT * FROM search_history ORDER BY date DESC")
    fun getAll(): LiveData<List<SearchHistoryEntity>>

    @Query("SELECT * FROM search_history WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): SearchHistoryEntity?

    @Query("DELETE FROM search_history")
    suspend fun deleteAll()
}
