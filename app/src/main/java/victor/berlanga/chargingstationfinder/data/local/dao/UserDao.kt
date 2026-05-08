package victor.berlanga.chargingstationfinder.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import victor.berlanga.chargingstationfinder.data.local.entity.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity): Long

    @Update
    suspend fun update(user: UserEntity)

    @Query("SELECT * FROM users ORDER BY id DESC")
    fun getAll(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): UserEntity?

    @Query("SELECT * FROM users ORDER BY id DESC LIMIT 1")
    suspend fun getLastUser(): UserEntity?
}
