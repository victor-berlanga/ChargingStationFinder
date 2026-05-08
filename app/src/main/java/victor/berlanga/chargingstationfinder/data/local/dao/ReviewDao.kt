package victor.berlanga.chargingstationfinder.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import victor.berlanga.chargingstationfinder.data.local.entity.ReviewEntity

@Dao
interface ReviewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(review: ReviewEntity): Long

    @Delete
    suspend fun delete(review: ReviewEntity)

    @Query("SELECT * FROM reviews ORDER BY date DESC")
    fun getAll(): LiveData<List<ReviewEntity>>

    @Query("SELECT * FROM reviews WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): ReviewEntity?

    @Query("SELECT * FROM reviews WHERE stationId = :stationId ORDER BY date DESC")
    fun getReviewsByStationId(stationId: Int): LiveData<List<ReviewEntity>>
}
