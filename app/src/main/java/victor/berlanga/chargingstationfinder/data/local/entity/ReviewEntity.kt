package victor.berlanga.chargingstationfinder.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val stationId: Int,
    val comment: String,
    val rating: Int,
    val date: Long = System.currentTimeMillis()
)
