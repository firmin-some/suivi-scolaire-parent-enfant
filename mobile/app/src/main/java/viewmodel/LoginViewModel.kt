package bf.ujkz.suiviscolaireparent.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import bf.ujkz.suiviscolaireparent.repository.AuthRepository
import bf.ujkz.suiviscolaireparent.repository.AuthResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoggedIn: Boolean = false
)

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository(application)

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(value: String) {
        _uiState.value = _uiState.value.copy(email = value, errorMessage = null)
    }

    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(password = value, errorMessage = null)
    }

    fun login() {
        val state = _uiState.value
        if (state.email.isBlank() || state.password.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Veuillez remplir tous les champs")
            return
        }
        _uiState.value = state.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            val result = repository.login(state.email, state.password)
            _uiState.value = when (result) {
                is AuthResult.Success -> _uiState.value.copy(isLoading = false, isLoggedIn = true)
                is AuthResult.Error -> _uiState.value.copy(isLoading = false, errorMessage = result.message)
            }
        }
    }
}