package bf.ujkz.suiviscolaireparent.repository

import android.content.Context
import bf.ujkz.suiviscolaireparent.network.ApiConfig
import bf.ujkz.suiviscolaireparent.network.NotificationDto
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import java.io.IOException

sealed class NotificationResult {
    data class Success(val data: List<NotificationDto>) : NotificationResult()
    data class Error(val message: String) : NotificationResult()
}

class NotificationRepository(context: Context) {

    private val tokenManager = TokenManager(context)
    private val apiService = ApiConfig.apiService

    suspend fun getNotifications(): NotificationResult {
        return try {
            val token = tokenManager.tokenFlow.first()
                ?: return NotificationResult.Error("Session expirée.")
            NotificationResult.Success(apiService.getNotifications("Bearer $token"))
        } catch (e: HttpException) {
            NotificationResult.Error("Erreur serveur (code ${e.code()})")
        } catch (e: IOException) {
            NotificationResult.Error("Impossible de contacter le serveur.")
        }
    }

    suspend fun marquerLu(id: Int) {
        try {
            val token = tokenManager.tokenFlow.first() ?: return
            apiService.marquerNotificationLue("Bearer $token", id)
        } catch (e: Exception) {
            // ignore
        }
    }
}