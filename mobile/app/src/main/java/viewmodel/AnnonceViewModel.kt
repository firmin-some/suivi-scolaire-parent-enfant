package bf.ujkz.suiviscolaireparent.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import bf.ujkz.suiviscolaireparent.network.AnnonceDto
import bf.ujkz.suiviscolaireparent.network.NotificationDto
import bf.ujkz.suiviscolaireparent.repository.AnnonceRepository
import bf.ujkz.suiviscolaireparent.repository.AnnonceResult
import bf.ujkz.suiviscolaireparent.repository.NotificationRepository
import bf.ujkz.suiviscolaireparent.repository.NotificationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AnnonceUiState(
    val isLoading: Boolean = true,
    val annonces: List<AnnonceDto> = emptyList(),
    val notifications: List<NotificationDto> = emptyList(),
    val errorMessage: String? = null
)

class AnnonceViewModel(application: Application) : AndroidViewModel(application) {

    private val annonceRepository = AnnonceRepository(application)
    private val notificationRepository = NotificationRepository(application)

    private val _uiState = MutableStateFlow(AnnonceUiState())
    val uiState: StateFlow<AnnonceUiState> = _uiState.asStateFlow()

    init { load() }

    fun load() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            val annonces = when (val r = annonceRepository.getAnnonces()) {
                is AnnonceResult.Success -> r.data
                is AnnonceResult.Error -> emptyList()
            }
            val notifications = when (val r = notificationRepository.getNotifications()) {
                is NotificationResult.Success -> r.data
                is NotificationResult.Error -> emptyList()
            }
            _uiState.value = AnnonceUiState(isLoading = false, annonces = annonces, notifications = notifications)
        }
    }

    fun marquerLu(id: Int) {
        viewModelScope.launch {
            notificationRepository.marquerLu(id)
            _uiState.value = _uiState.value.copy(
                notifications = _uiState.value.notifications.map {
                    if (it.id == id) it.copy(lu = true) else it
                }
            )
        }
    }
}