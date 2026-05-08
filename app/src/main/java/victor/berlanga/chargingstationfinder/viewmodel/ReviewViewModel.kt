package victor.berlanga.chargingstationfinder.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import victor.berlanga.chargingstationfinder.data.local.entity.ReviewEntity
import victor.berlanga.chargingstationfinder.data.repository.AppRepository
import victor.berlanga.chargingstationfinder.util.ValidationUtils

class ReviewViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepository.getInstance(application)
    private val stationId = MutableLiveData<Int>()

    val reviews: LiveData<List<ReviewEntity>> = stationId.switchMap { id ->
        repository.getReviewsByStationId(id)
    }

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun loadReviews(stationId: Int) {
        this.stationId.value = stationId
    }

    fun addReview(stationId: Int, comment: String, rating: Int) {
        if (!ValidationUtils.isValidReview(comment, rating)) {
            _errorMessage.value = "Agrega un comentario y una calificación de 1 a 5"
            return
        }

        viewModelScope.launch {
            repository.addReview(stationId, comment, rating)
            _errorMessage.value = null
        }
    }
}
