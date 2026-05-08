package victor.berlanga.chargingstationfinder

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import victor.berlanga.chargingstationfinder.data.local.entity.StationEntity
import victor.berlanga.chargingstationfinder.util.FavoriteUtils
import victor.berlanga.chargingstationfinder.util.StationFilterUtils
import victor.berlanga.chargingstationfinder.util.ValidationUtils

class ChargingStationLogicTest {

    private val stations = listOf(
        StationEntity(
            id = 1,
            name = "Centro Monterrey",
            address = "Av. Juarez 100",
            municipality = "Monterrey",
            latitude = 25.67,
            longitude = -100.31,
            usageType = "Public",
            chargerCount = 2,
            power = "50kW",
            connectorStandard = "CCS;J1772",
            connectorCountByStandard = "1;1",
            currentType = "DC",
            mainUse = "Espacio publico",
            insideOf = "Independiente",
            observations = "Disponible"
        ),
        StationEntity(
            id = 2,
            name = "Universidad Sur",
            address = "Garza Sada 2501",
            municipality = "San Pedro Garza Garcia",
            latitude = 25.65,
            longitude = -100.33,
            usageType = "Private",
            chargerCount = 4,
            power = "7kW",
            connectorStandard = "Type 2",
            connectorCountByStandard = "4",
            currentType = "AC",
            mainUse = "Universidad",
            insideOf = "Campus",
            observations = "Solo visitantes"
        )
    )

    @Test
    fun emptyUserNameFails() {
        assertFalse(ValidationUtils.isValidUser("", "user@mail.com"))
    }

    @Test
    fun blankUserNameFails() {
        assertFalse(ValidationUtils.isValidUser("   ", "user@mail.com"))
    }

    @Test
    fun emptyEmailFails() {
        assertFalse(ValidationUtils.isValidUser("Victor", ""))
    }

    @Test
    fun validUserRegistrationPasses() {
        assertTrue(ValidationUtils.isValidUser("Victor", "victor@mail.com"))
    }

    @Test
    fun searchStationsByMunicipalityReturnsMatches() {
        val result = StationFilterUtils.search(stations, "Monterrey")
        assertEquals(1, result.size)
        assertEquals("Centro Monterrey", result.first().name)
    }

    @Test
    fun searchStationsByConnectorStandardReturnsMatches() {
        val result = StationFilterUtils.search(stations, "type 2")
        assertEquals(1, result.size)
        assertEquals("Universidad Sur", result.first().name)
    }

    @Test
    fun filterPublicUsageReturnsOnlyPublicStations() {
        val result = StationFilterUtils.filterByUsageType(stations, "Public")
        assertEquals(1, result.size)
        assertEquals("Public", result.first().usageType)
    }

    @Test
    fun filterPrivateUsageReturnsOnlyPrivateStations() {
        val result = StationFilterUtils.filterByUsageType(stations, "Private")
        assertEquals(1, result.size)
        assertEquals("Private", result.first().usageType)
    }

    @Test
    fun ratingBelowOneIsInvalid() {
        assertFalse(ValidationUtils.isValidReview("Buen cargador", 0))
    }

    @Test
    fun ratingAboveFiveIsInvalid() {
        assertFalse(ValidationUtils.isValidReview("Buen cargador", 6))
    }

    @Test
    fun validReviewPasses() {
        assertTrue(ValidationUtils.isValidReview("Funciono bien", 5))
    }

    @Test
    fun favoriteToggleAddsStationWhenMissing() {
        val result = FavoriteUtils.toggleFavorite(emptySet(), 3)
        assertTrue(result.contains(3))
    }

    @Test
    fun favoriteToggleRemovesStationWhenAlreadySaved() {
        val result = FavoriteUtils.toggleFavorite(setOf(3), 3)
        assertFalse(result.contains(3))
    }
}
