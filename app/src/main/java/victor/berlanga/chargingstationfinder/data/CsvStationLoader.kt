package victor.berlanga.chargingstationfinder.data

import android.content.Context
import victor.berlanga.chargingstationfinder.data.local.entity.StationEntity

object CsvStationLoader {

    fun loadStations(context: Context): List<StationEntity> {
        val csv = context.assets.open("centros_de_carga.csv")
            .bufferedReader()
            .use { it.readText() }

        return parseRows(csv)
            .drop(1)
            .mapNotNull { row -> row.toStationOrNull() }
    }

    private fun List<String>.toStationOrNull(): StationEntity? {
        if (size < 15) return null

        val id = getOrNull(0)?.trim()?.toIntOrNull() ?: return null
        return StationEntity(
            id = id,
            name = clean(1),
            address = clean(2),
            municipality = clean(3),
            latitude = clean(4).toDoubleOrNull() ?: 0.0,
            longitude = clean(5).toDoubleOrNull() ?: 0.0,
            usageType = clean(6),
            chargerCount = clean(7).toIntOrNull() ?: 0,
            power = clean(8),
            connectorStandard = clean(9),
            connectorCountByStandard = clean(10),
            currentType = clean(11),
            mainUse = clean(12),
            insideOf = clean(13),
            observations = clean(14)
        )
    }

    private fun List<String>.clean(index: Int): String {
        return getOrNull(index)?.trim()?.removePrefix("\uFEFF").orEmpty()
    }

    private fun parseRows(csv: String): List<List<String>> {
        val rows = mutableListOf<List<String>>()
        val row = mutableListOf<String>()
        val value = StringBuilder()
        var insideQuotes = false
        var index = 0

        while (index < csv.length) {
            val char = csv[index]
            when {
                char == '"' && insideQuotes && index + 1 < csv.length && csv[index + 1] == '"' -> {
                    value.append('"')
                    index++
                }
                char == '"' -> insideQuotes = !insideQuotes
                char == ',' && !insideQuotes -> {
                    row.add(value.toString())
                    value.clear()
                }
                (char == '\n' || char == '\r') && !insideQuotes -> {
                    if (char == '\r' && index + 1 < csv.length && csv[index + 1] == '\n') {
                        index++
                    }
                    row.add(value.toString())
                    value.clear()
                    if (row.any { it.isNotBlank() }) {
                        rows.add(row.toList())
                    }
                    row.clear()
                }
                else -> value.append(char)
            }
            index++
        }

        if (value.isNotEmpty() || row.isNotEmpty()) {
            row.add(value.toString())
            rows.add(row.toList())
        }

        return rows
    }
}
