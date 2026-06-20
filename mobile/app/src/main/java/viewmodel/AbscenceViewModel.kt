package bf.ujkz.suiviscolaireparent.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import bf.ujkz.suiviscolaireparent.network.AbsenceDto
import bf.ujkz.suiviscolaireparent.repository.AbsenceRepository
import bf.ujkz.suiviscolaireparent.repository.AbsenceResult
import bf.ujkz.suiviscolaireparent.repository.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class AbsenceUiState(
    val isLoading: Boolean = true,
    val absences: List<AbsenceDto> = emptyList(),
    val errorMessage: String? = null
)

class AbsenceViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AbsenceRepository(application)
    private val tokenManager = TokenManager(application)

    private val _uiState = MutableStateFlow(AbsenceUiState())
    val uiState: StateFlow<AbsenceUiState> = _uiState.asStateFlow()

    init { load() }

    fun load() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            val eleveId = tokenManager.eleveIdFlow.first()
            if (eleveId == null) {
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = "Aucun enfant sélectionné.")
                return@launch
            }
            when (val result = repository.getAbsences(eleveId)) {
                is AbsenceResult.Success -> _uiState.value = _uiState.value.copy(isLoading = false, absences = result.data)
                is AbsenceResult.Error -> _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = result.message)
            }
        }
    }
}