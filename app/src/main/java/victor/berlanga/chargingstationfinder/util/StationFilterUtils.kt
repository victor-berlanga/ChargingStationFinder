package victor.berlanga.chargingstationfinder.util

import victor.berlanga.chargingstationfinder.data.local.entity.StationEntity

object StationFilterUtils {

    fun search(stations: List<StationEntity>, text: String): List<StationEntity> {
        val query = text.trim().lowercase()
        if (query.isEmpty()) return stations

        return stations.filter { station ->
            station.name.lowercase().contains(query) ||
                station.address.lowercase().contains(query) ||
                station.municipality.lowercase().contains(query) ||
                station.connectorStandard.lowercase().contains(query)
        }
    }

    fun filterByUsageType(
        stations: List<StationEntity>,
        usageType: String
    ): List<StationEntity> {
        val type = usageType.trim().lowercase()
        if (type.isEmpty()) return stations

        return stations.filter { station ->
            station.usageType.lowercase().contains(type)
        }
    }
}
