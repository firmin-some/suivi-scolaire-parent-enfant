package bf.ujkz.suiviscolaireparent.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import bf.ujkz.suiviscolaireparent.network.NotesTrimestreDto
import bf.ujkz.suiviscolaireparent.repository.BulletinResult
import bf.ujkz.suiviscolaireparent.repository.NoteRepository
import bf.ujkz.suiviscolaireparent.repository.NotesResult
import bf.ujkz.suiviscolaireparent.repository.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class NoteUiState(
    val isLoading: Boolean = true,
    val data: NotesTrimestreDto? = null,
    val trimestre: Int = 1,
    val errorMessage: String? = null,
    val bulletinUrl: String? = null,
    val isLoadingBulletin: Boolean = false,
    val bulletinError: String? = null
)

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = NoteRepository(application)
    private val tokenManager = TokenManager(application)

    private val _uiState = MutableStateFlow(NoteUiState())
    val uiState: StateFlow<NoteUiState> = _uiState.asStateFlow()

    init { loadNotes() }

    fun selectTrimestre(t: Int) {
        _uiState.value = _uiState.value.copy(trimestre = t)
        loadNotes()
    }

    fun loadNotes() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            val eleveId = tokenManager.eleveIdFlow.first()
            if (eleveId == null) {
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = "Aucun enfant sélectionné.")
                return@launch
            }
            when (val result = repository.getNotes(eleveId, _uiState.value.trimestre)) {
                is NotesResult.Success -> _uiState.value = _uiState.value.copy(isLoading = false, data = result.data)
                is NotesResult.Error -> _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = result.message)
            }
        }
    }

    fun downloadBulletin() {
        _uiState.value = _uiState.value.copy(isLoadingBulletin = true, bulletinError = null, bulletinUrl = null)
        viewModelScope.launch {
            val eleveId = tokenManager.eleveIdFlow.first()
            if (eleveId == null) {
                _uiState.value = _uiState.value.copy(isLoadingBulletin = false, bulletinError = "Aucun enfant sélectionné.")
                return@launch
            }
            when (val result = repository.getBulletinUrl(eleveId, _uiState.value.trimestre)) {
                is BulletinResult.Success -> _uiState.value = _uiState.value.copy(isLoadingBulletin = false, bulletinUrl = result.url)
                is BulletinResult.Error -> _uiState.value = _uiState.value.copy(isLoadingBulletin = false, bulletinError = result.message)
            }
        }
    }

    fun bulletinHandled() {
        _uiState.value = _uiState.value.copy(bulletinUrl = null)
    }
}