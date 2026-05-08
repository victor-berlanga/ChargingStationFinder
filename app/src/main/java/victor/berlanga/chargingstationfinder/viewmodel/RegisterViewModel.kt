package victor.berlanga.chargingstationfinder.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import victor.berlanga.chargingstationfinder.data.repository.AppRepository
import victor.berlanga.chargingstationfinder.util.ValidationUtils

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepository.getInstance(application)

    private val _registrationSaved = MutableLiveData<Boolean>()
    val registrationSaved: LiveData<Boolean> = _registrationSaved

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun saveUser(name: String, email: String) {
        if (!ValidationUtils.isValidUser(name, email)) {
            _errorMessage.value = "Nombre y correo son obligatorios"
            _registrationSaved.value = false
            return
        }

        viewModelScope.launch {
            repository.saveUserRegistration(name, email)
            _errorMessage.value = null
            _registrationSaved.value = true
        }
    }
}
