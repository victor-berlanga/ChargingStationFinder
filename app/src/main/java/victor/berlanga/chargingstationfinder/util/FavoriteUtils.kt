package victor.berlanga.chargingstationfinder.util

object FavoriteUtils {

    fun toggleFavorite(favorites: Set<Int>, stationId: Int): Set<Int> {
        return if (favorites.contains(stationId)) {
            favorites - stationId
        } else {
            favorites + stationId
        }
    }
}
