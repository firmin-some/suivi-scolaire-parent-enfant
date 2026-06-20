package bf.ujkz.suiviscolaireparent.repository

import android.content.Context
import bf.ujkz.suiviscolaireparent.network.ApiConfig
import bf.ujkz.suiviscolaireparent.network.LoginRequest
import bf.ujkz.suiviscolaireparent.network.ParentDto
import bf.ujkz.suiviscolaireparent.network.UpdatePasswordRequest
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import java.io.IOException

sealed class AuthResult {
    data class Success(val parent: ParentDto) : AuthResult()
    data class Error(val message: String) : AuthResult()
}

class AuthRepository(context: Context) {

    private val tokenManager = TokenManager(context)
    private val apiService = ApiConfig.apiService

    suspend fun login(email: String, password: String): AuthResult {
        return try {
            val response = apiService.login(LoginRequest(email, password))
            tokenManager.saveToken(response.token)
            tokenManager.saveCivilite(response.parent.civilite)
            AuthResult.Success(response.parent)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            AuthResult.Error(errorBody ?: "Erreur serveur (code ${e.code()})")
        } catch (e: IOException) {
            AuthResult.Error("Impossible de contacter le serveur. Vérifiez votre connexion.")
        }
    }

    suspend fun logout() {
        try {
            val token = tokenManager.tokenFlow.first()
            if (token != null) {
                apiService.logout("Bearer $token")
            }
        } catch (e: Exception) {
            // On ignore l'erreur réseau ici : on déconnecte localement quoi qu'il arrive
        } finally {
            tokenManager.clearToken()
        }
    }

    suspend fun updatePassword(ancienMdp: String, nouveauMdp: String): AuthResult {
        return try {
            val token = tokenManager.tokenFlow.first()
                ?: return AuthResult.Error("Session expirée.")
            apiService.updatePassword(
                "Bearer $token",
                UpdatePasswordRequest(ancienMdp, nouveauMdp, nouveauMdp)
            )
            AuthResult.Success(ParentDto(0, "", "", "", null))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            AuthResult.Error(errorBody ?: "Erreur serveur (code ${e.code()})")
        } catch (e: IOException) {
            AuthResult.Error("Impossible de contacter le serveur.")
        }
    }
}