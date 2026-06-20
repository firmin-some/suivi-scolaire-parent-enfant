package bf.ujkz.suiviscolaireparent.repository

import android.content.Context
import bf.ujkz.suiviscolaireparent.network.AbsenceDto
import bf.ujkz.suiviscolaireparent.network.ApiConfig
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import java.io.IOException

sealed class AbsenceResult {
    data class Success(val data: List<AbsenceDto>) : AbsenceResult()
    data class Error(val message: String) : AbsenceResult()
}

class AbsenceRepository(context: Context) {

    private val tokenManager = TokenManager(context)
    private val apiService = ApiConfig.apiService

    suspend fun getAbsences(eleveId: Int): AbsenceResult {
        return try {
            val token = tokenManager.tokenFlow.first()
                ?: return AbsenceResult.Error("Session expirée.")
            AbsenceResult.Success(apiService.getAbsences("Bearer $token", eleveId))
        } catch (e: HttpException) {
            AbsenceResult.Error("Erreur serveur (code ${e.code()})")
        } catch (e: IOException) {
            AbsenceResult.Error("Impossible de contacter le serveur.")
        }
    }
}