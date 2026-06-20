package bf.ujkz.suiviscolaireparent.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import bf.ujkz.suiviscolaireparent.network.PaiementsDto
import bf.ujkz.suiviscolaireparent.repository.CreatePaiementResult
import bf.ujkz.suiviscolaireparent.repository.PaiementRepository
import bf.ujkz.suiviscolaireparent.repository.PaiementResult
import bf.ujkz.suiviscolaireparent.repository.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class PaiementUiState(
    val isLoading: Boolean = true,
    val data: PaiementsDto? = null,
    val errorMessage: String? = null,
    val isSubmitting: Boolean = false,
    val submitSuccess: Boolean = false,
    val submitError: String? = null
)

class PaiementViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PaiementRepository(application)
    private val tokenManager = TokenManager(application)

    private val _uiState = MutableStateFlow(PaiementUiState())
    val uiState: StateFlow<PaiementUiState> = _uiState.asStateFlow()

    init { load() }

    fun load() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            val eleveId = tokenManager.eleveIdFlow.first()
            if (eleveId == null) {
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = "Aucun enfant sélectionné.")
                return@launch
            }
            when (val result = repository.getPaiements(eleveId)) {
                is PaiementResult.Success -> _uiState.value = _uiState.value.copy(isLoading = false, data = result.data)
                is PaiementResult.Error -> _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = result.message)
            }
        }
    }

    fun submitPaiement(montant: Int, modePaiement: String) {
        _uiState.value = _uiState.value.copy(isSubmitting = true, submitError = null, submitSuccess = false)
        viewModelScope.launch {
            val eleveId = tokenManager.eleveIdFlow.first()
            if (eleveId == null) {
                _uiState.value = _uiState.value.copy(isSubmitting = false, submitError = "Aucun enfant sélectionné.")
                return@launch
            }
            when (val result = repository.createPaiement(eleveId, montant, modePaiement)) {
                is CreatePaiementResult.Success -> {
                    _uiState.value = _uiState.value.copy(isSubmitting = false, submitSuccess = true)
                    load()
                }
                is CreatePaiementResult.Error -> {
                    _uiState.value = _uiState.value.copy(isSubmitting = false, submitError = result.message)
                }
            }
        }
    }

    fun resetSubmitState() {
        _uiState.value = _uiState.value.copy(submitSuccess = false, submitError = null)
    }
}