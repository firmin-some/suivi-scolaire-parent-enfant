package bf.ujkz.suiviscolaireparent.network

import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("logout")
    suspend fun logout(@Header("Authorization") token: String): ResponseBody

    @GET("eleves")
    suspend fun getEleves(@Header("Authorization") token: String): List<EleveDto>

    @GET("eleves/{id}")
    suspend fun getEleveDetail(@Header("Authorization") token: String, @Path("id") id: Int): EleveDetailDto

    @GET("eleves/{id}/notes")
    suspend fun getNotes(
        @Header("Authorization") token: String,
        @Path("id") eleveId: Int,
        @Query("trimestre") trimestre: Int = 1
    ): NotesTrimestreDto

    @GET("eleves/{id}/paiements")
    suspend fun getPaiements(@Header("Authorization") token: String, @Path("id") eleveId: Int): PaiementsDto

    @POST("eleves/{id}/paiements")
    suspend fun createPaiement(
        @Header("Authorization") token: String,
        @Path("id") eleveId: Int,
        @Body request: PaiementCreateRequest
    ): VersementDto

    @GET("eleves/{id}/absences")
    suspend fun getAbsences(@Header("Authorization") token: String, @Path("id") eleveId: Int): List<AbsenceDto>

    @GET("eleves/{id}/bulletin")
    suspend fun getBulletin(
        @Header("Authorization") token: String,
        @Path("id") eleveId: Int,
        @Query("trimestre") trimestre: Int
    ): BulletinDto

    @GET("annonces")
    suspend fun getAnnonces(@Header("Authorization") token: String): List<AnnonceDto>
    @PUT("password")
    suspend fun updatePassword(
        @Header("Authorization") token: String,
        @Body request: UpdatePasswordRequest
    ): ResponseBody
    @POST("eleves/verify")
    suspend fun verifyEleve(
        @Header("Authorization") token: String,
        @Body request: VerifyChildRequest
    ): EleveDto
}