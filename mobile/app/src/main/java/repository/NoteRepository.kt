package bf.ujkz.suiviscolaireparent.repository

import android.content.Context
import bf.ujkz.suiviscolaireparent.network.ApiConfig
import bf.ujkz.suiviscolaireparent.network.NotesTrimestreDto
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import java.io.IOException

sealed class NotesResult {
    data class Success(val data: NotesTrimestreDto) : NotesResult()
    data class Error(val message: String) : NotesResult()
}

sealed class BulletinResult {
    data class Success(val url: String) : BulletinResult()
    data class Error(val message: String) : BulletinResult()
}

class NoteRepository(context: Context) {

    private val tokenManager = TokenManager(context)
    private val apiService = ApiConfig.apiService

    suspend fun getNotes(eleveId: Int, trimestre: Int): NotesResult {
        return try {
            val token = tokenManager.tokenFlow.first() ?: return NotesResult.Error("Session expirée.")
            val data = apiService.getNotes("Bearer $token", eleveId, trimestre)
            NotesResult.Success(data)
        } catch (e: HttpException) {
            NotesResult.Error("Erreur serveur (code ${e.code()})")
        } catch (e: IOException) {
            NotesResult.Error("Impossible de contacter le serveur.")
        }
    }

    suspend fun getBulletinUrl(eleveId: Int, trimestre: Int): BulletinResult {
        return try {
            val token = tokenManager.tokenFlow.first() ?: return BulletinResult.Error("Session expirée.")
            val result = apiService.getBulletin("Bearer $token", eleveId, trimestre)
            BulletinResult.Success(result.bulletin_url)
        } catch (e: HttpException) {
            BulletinResult.Error("Erreur serveur (code ${e.code()})")
        } catch (e: IOException) {
            BulletinResult.Error("Impossible de contacter le serveur.")
        }
    }
}