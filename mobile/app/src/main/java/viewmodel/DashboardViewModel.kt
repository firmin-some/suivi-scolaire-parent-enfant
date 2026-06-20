package bf.ujkz.suiviscolaireparent.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import bf.ujkz.suiviscolaireparent.network.EleveDetailDto
import bf.ujkz.suiviscolaireparent.repository.AuthRepository
import bf.ujkz.suiviscolaireparent.repository.AuthResult
import bf.ujkz.suiviscolaireparent.repository.DashboardResult
import bf.ujkz.suiviscolaireparent.repository.StudentRepository
import bf.ujkz.suiviscolaireparent.repository.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class DashboardUiState(
    val isLoading: Boolean = true,
    val eleve: EleveDetailDto? = null,
    val errorMessage: String? = null,
    val parentLabel: String = "Parent",
    val showPasswordDialog: Boolean = false,
    val ancienMdp: String = "",
    val nouveauMdp: String = "",
    val confirmMdp: String = "",
    val isUpdatingPassword: Boolean = false,
    val passwordSuccess: Boolean = false,
    val passwordError: String? = null
)

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val studentRepository = StudentRepository(application)
    private val authRepository = AuthRepository(application)
    private val tokenManager = TokenManager(application)

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboard()
        loadParentLabel()
    }

    private fun loadParentLabel() {
        viewModelScope.launch {
            val civilite = tokenManager.civiliteFlow.first()
            val label = when (civilite) {
                "M" -> "Papa"
                "Mme" -> "Maman"
                else -> "Parent"
            }
            _uiState.value = _uiState.value.copy(parentLabel = label)
        }
    }

    fun loadDashboard() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            when (val result = studentRepository.loadDashboard()) {
                is DashboardResult.Success -> _uiState.value = _uiState.value.copy(isLoading = false, eleve = result.eleve)
                is DashboardResult.Error -> _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = result.message)
            }
        }
    }

    fun showPasswordDialog() { _uiState.value = _uiState.value.copy(showPasswordDialog = true, passwordError = null, passwordSuccess = false) }
    fun hidePasswordDialog() { _uiState.value = _uiState.value.copy(showPasswordDialog = false, ancienMdp = "", nouveauMdp = "", confirmMdp = "") }
    fun onAncienMdpChange(v: String) { _uiState.value = _uiState.value.copy(ancienMdp = v) }
    fun onNouveauMdpChange(v: String) { _uiState.value = _uiState.value.copy(nouveauMdp = v) }
    fun onConfirmMdpChange(v: String) { _uiState.value = _uiState.value.copy(confirmMdp = v) }

    fun submitPasswordUpdate() {
        val s = _uiState.value
        if (s.nouveauMdp != s.confirmMdp) {
            _uiState.value = s.copy(passwordError = "Les deux nouveaux mots de passe ne correspondent pas.")
            return
        }
        if (s.nouveauMdp.length < 6) {
            _uiState.value = s.copy(passwordError = "Le nouveau mot de passe doit contenir au moins 6 caractères.")
            return
        }
        _uiState.value = s.copy(isUpdatingPassword = true, passwordError = null)
        viewModelScope.launch {
            when (val result = authRepository.updatePassword(s.ancienMdp, s.nouveauMdp)) {
                is AuthResult.Success -> _uiState.value = _uiState.value.copy(isUpdatingPassword = false, passwordSuccess = true)
                is AuthResult.Error -> _uiState.value = _uiState.value.copy(isUpdatingPassword = false, passwordError = result.message)
            }
        }
    }
}