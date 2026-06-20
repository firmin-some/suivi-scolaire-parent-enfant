package bf.ujkz.suiviscolaireparent.repository

import android.content.Context
import bf.ujkz.suiviscolaireparent.network.AnnonceDto
import bf.ujkz.suiviscolaireparent.network.ApiConfig
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import java.io.IOException

sealed class AnnonceResult {
    data class Success(val data: List<AnnonceDto>) : AnnonceResult()
    data class Error(val message: String) : AnnonceResult()
}

class AnnonceRepository(context: Context) {

    private val tokenManager = TokenManager(context)
    private val apiService = ApiConfig.apiService

    suspend fun getAnnonces(): AnnonceResult {
        return try {
            val token = tokenManager.tokenFlow.first()
                ?: return AnnonceResult.Error("Session expirée.")
            AnnonceResult.Success(apiService.getAnnonces("Bearer $token"))
        } catch (e: HttpException) {
            AnnonceResult.Error("Erreur serveur (code ${e.code()})")
        } catch (e: IOException) {
            AnnonceResult.Error("Impossible de contacter le serveur.")
        }
    }
}