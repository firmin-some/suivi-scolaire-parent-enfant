package bf.ujkz.suiviscolaireparent.repository

import android.content.Context
import bf.ujkz.suiviscolaireparent.network.ApiConfig
import bf.ujkz.suiviscolaireparent.network.EleveDetailDto
import bf.ujkz.suiviscolaireparent.network.EleveDto
import bf.ujkz.suiviscolaireparent.network.VerifyChildRequest
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import java.io.IOException

sealed class DashboardResult {
    data class Success(val eleve: EleveDetailDto) : DashboardResult()
    data class Error(val message: String) : DashboardResult()
}

sealed class VerifyResult {
    data class Success(val eleve: EleveDto) : VerifyResult()
    data class Error(val message: String) : VerifyResult()
}

class StudentRepository(context: Context) {

    private val tokenManager = TokenManager(context)
    private val apiService = ApiConfig.apiService

    suspend fun verifyChild(nom: String, classe: String): VerifyResult {
        return try {
            val token = tokenManager.tokenFlow.first()
                ?: return VerifyResult.Error("Session expirée, veuillez vous reconnecter.")
            val eleve = apiService.verifyEleve("Bearer $token", VerifyChildRequest(nom, classe))
            tokenManager.saveEleveId(eleve.id)
            VerifyResult.Success(eleve)
        } catch (e: HttpException) {
            if (e.code() == 404 || e.code() == 403) {
                VerifyResult.Error("Aucun enfant ne correspond à ces informations. Vérifiez le nom et la classe.")
            } else {
                VerifyResult.Error("Erreur serveur (code ${e.code()})")
            }
        } catch (e: IOException) {
            VerifyResult.Error("Impossible de contacter le serveur.")
        }
    }

    suspend fun loadDashboard(): DashboardResult {
        return try {
            val token = tokenManager.tokenFlow.first()
                ?: return DashboardResult.Error("Session expirée, veuillez vous reconnecter.")
            val authHeader = "Bearer $token"

            val storedEleveId = tokenManager.eleveIdFlow.first()
            val eleveId = storedEleveId ?: run {
                val eleves = apiService.getEleves(authHeader)
                eleves.firstOrNull()?.id
            } ?: return DashboardResult.Error("Aucun élève associé à ce compte.")

            val detail = apiService.getEleveDetail(authHeader, eleveId)
            DashboardResult.Success(detail)
        } catch (e: HttpException) {
            DashboardResult.Error("Erreur serveur (code ${e.code()})")
        } catch (e: IOException) {
            DashboardResult.Error("Impossible de contacter le serveur.")
        }
    }
}