package bf.ujkz.suiviscolaireparent.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import bf.ujkz.suiviscolaireparent.network.AnnonceDto
import bf.ujkz.suiviscolaireparent.repository.AnnonceRepository
import bf.ujkz.suiviscolaireparent.repository.AnnonceResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AnnonceUiState(
    val isLoading: Boolean = true,
    val annonces: List<AnnonceDto> = emptyList(),
    val errorMessage: String? = null
)

class AnnonceViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AnnonceRepository(application)

    private val _uiState = MutableStateFlow(AnnonceUiState())
    val uiState: StateFlow<AnnonceUiState> = _uiState.asStateFlow()

    init { load() }

    fun load() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            when (val result = repository.getAnnonces()) {
                is AnnonceResult.Success -> _uiState.value = AnnonceUiState(isLoading = false, annonces = result.data)
                is AnnonceResult.Error -> _uiState.value = AnnonceUiState(isLoading = false, errorMessage = result.message)
            }
        }
    }
}