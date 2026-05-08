package victor.berlanga.chargingstationfinder.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorite_stations",
    indices = [Index(value = ["stationId"], unique = true)]
)
data class FavoriteStationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val stationId: Int,
    val savedAt: Long = System.currentTimeMillis()
)
