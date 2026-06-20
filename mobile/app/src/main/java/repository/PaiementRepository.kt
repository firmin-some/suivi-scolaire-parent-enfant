package bf.ujkz.suiviscolaireparent.repository

import android.content.Context
import bf.ujkz.suiviscolaireparent.network.ApiConfig
import bf.ujkz.suiviscolaireparent.network.PaiementCreateRequest
import bf.ujkz.suiviscolaireparent.network.PaiementsDto
import bf.ujkz.suiviscolaireparent.network.VersementDto
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import java.io.IOException

sealed class PaiementResult {
    data class Success(val data: PaiementsDto) : PaiementResult()
    data class Error(val message: String) : PaiementResult()
}

sealed class CreatePaiementResult {
    data class Success(val versement: VersementDto) : CreatePaiementResult()
    data class Error(val message: String) : CreatePaiementResult()
}

class PaiementRepository(context: Context) {

    private val tokenManager = TokenManager(context)
    private val apiService = ApiConfig.apiService

    suspend fun getPaiements(eleveId: Int): PaiementResult {
        return try {
            val token = tokenManager.tokenFlow.first() ?: return PaiementResult.Error("Session expirée.")
            PaiementResult.Success(apiService.getPaiements("Bearer $token", eleveId))
        } catch (e: HttpException) {
            PaiementResult.Error("Erreur serveur (code ${e.code()})")
        } catch (e: IOException) {
            PaiementResult.Error("Impossible de contacter le serveur.")
        }
    }

    suspend fun createPaiement(eleveId: Int, montant: Int, modePaiement: String): CreatePaiementResult {
        return try {
            val token = tokenManager.tokenFlow.first() ?: return CreatePaiementResult.Error("Session expirée.")
            val versement = apiService.createPaiement("Bearer $token", eleveId, PaiementCreateRequest(montant, modePaiement))
            CreatePaiementResult.Success(versement)
        } catch (e: HttpException) {
            CreatePaiementResult.Error("Erreur serveur (code ${e.code()})")
        } catch (e: IOException) {
            CreatePaiementResult.Error("Impossible de contacter le serveur.")
        }
    }

    suspend fun getBulletinUrl(eleveId: Int, trimestre: Int): CreatePaiementResult.Error? {
        // non utilisé ici, voir NoteRepository
        return null
    }

}