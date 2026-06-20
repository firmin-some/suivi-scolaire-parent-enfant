package bf.ujkz.suiviscolaireparent.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import bf.ujkz.suiviscolaireparent.repository.StudentRepository
import bf.ujkz.suiviscolaireparent.repository.VerifyResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class VerifyChildUiState(
    val nom: String = "",
    val classe: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val verified: Boolean = false
)

class VerifyChildViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = StudentRepository(application)

    private val _uiState = MutableStateFlow(VerifyChildUiState())
    val uiState: StateFlow<VerifyChildUiState> = _uiState.asStateFlow()

    fun onNomChange(v: String) { _uiState.value = _uiState.value.copy(nom = v, errorMessage = null) }
    fun onClasseChange(v: String) { _uiState.value = _uiState.value.copy(classe = v, errorMessage = null) }

    fun verify() {
        val s = _uiState.value
        if (s.nom.isBlank() || s.classe.isBlank()) {
            _uiState.value = s.copy(errorMessage = "Veuillez remplir le nom et la classe.")
            return
        }
        _uiState.value = s.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            when (val result = repository.verifyChild(s.nom.trim(), s.classe.trim())) {
                is VerifyResult.Success -> _uiState.value = _uiState.value.copy(isLoading = false, verified = true)
                is VerifyResult.Error -> _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = result.message)
            }
        }
    }
}