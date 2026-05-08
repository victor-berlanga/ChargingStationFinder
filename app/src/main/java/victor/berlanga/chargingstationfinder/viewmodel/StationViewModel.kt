package victor.berlanga.chargingstationfinder.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import victor.berlanga.chargingstationfinder.data.local.entity.StationEntity
import victor.berlanga.chargingstationfinder.data.repository.AppRepository

class StationViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepository.getInstance(application)

    private val searchText = MutableLiveData("")
    private val filterData = MutableLiveData(FilterData())

    val stations: LiveData<List<StationEntity>> = repository.stations
    val searchResults: LiveData<List<StationEntity>> = searchText.switchMap { query ->
        if (query.isBlank()) repository.stations else repository.searchStations(query)
    }
    val filteredStations: LiveData<List<StationEntity>> = filterData.switchMap { filter ->
        repository.filterStations(
            filter.text,
            filter.connector,
            filter.municipality,
            filter.usageType,
            filter.currentType
        )
    }

    private val _selectedStation = MutableLiveData<StationEntity?>()
    val selectedStation: LiveData<StationEntity?> = _selectedStation

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        loadInitialData()
    }

    fun loadInitialData() {
        viewModelScope.launch {
            _loading.value = true
            try {
                repository.seedInitialStationsIfNeeded()
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "No se pudieron cargar las estaciones"
            }
            _loading.value = false
        }
    }

    fun searchStations(query: String) {
        searchText.value = query
        viewModelScope.launch {
            repository.saveSearchHistory(query)
        }
    }

    fun filterStations(
        text: String,
        connector: String,
        municipality: String,
        usageType: String,
        currentType: String
    ) {
        filterData.value = FilterData(text, connector, municipality, usageType, currentType)
        viewModelScope.launch {
            repository.saveSearchHistory(text)
        }
    }

    fun selectStation(stationId: Int) {
        viewModelScope.launch {
            _selectedStation.value = repository.getStationById(stationId)
        }
    }

    private data class FilterData(
        val text: String = "",
        val connector: String = "",
        val municipality: String = "",
        val usageType: String = "",
        val currentType: String = ""
    )
}
