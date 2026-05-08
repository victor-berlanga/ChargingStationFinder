package victor.berlanga.chargingstationfinder.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stations")
data class StationEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val address: String,
    val municipality: String,
    val latitude: Double,
    val longitude: Double,
    val usageType: String,
    val chargerCount: Int,
    val power: String,
    val connectorStandard: String,
    val connectorCountByStandard: String,
    val currentType: String,
    val mainUse: String,
    val insideOf: String,
    val observations: String
)
