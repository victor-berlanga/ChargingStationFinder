package victor.berlanga.chargingstationfinder.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import victor.berlanga.chargingstationfinder.data.local.entity.StationEntity
import victor.berlanga.chargingstationfinder.data.repository.AppRepository

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepository.getInstance(application)

    val favorites: LiveData<List<StationEntity>> = repository.favoriteStations

    private val _isFavorite = MutableLiveData(false)
    val isFavorite: LiveData<Boolean> = _isFavorite

    fun checkFavorite(stationId: Int) {
        viewModelScope.launch {
            _isFavorite.value = repository.isFavorite(stationId)
        }
    }

    fun saveFavorite(stationId: Int) {
        viewModelScope.launch {
            repository.saveFavorite(stationId)
            _isFavorite.value = true
        }
    }

    fun removeFavorite(stationId: Int) {
        viewModelScope.launch {
            repository.removeFavorite(stationId)
            _isFavorite.value = false
        }
    }

    fun toggleFavorite(stationId: Int) {
        viewModelScope.launch {
            if (repository.isFavorite(stationId)) {
                repository.removeFavorite(stationId)
                _isFavorite.value = false
            } else {
                repository.saveFavorite(stationId)
                _isFavorite.value = true
            }
        }
    }
}
