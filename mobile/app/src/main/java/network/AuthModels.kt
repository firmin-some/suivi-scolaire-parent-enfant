package bf.ujkz.suiviscolaireparent.network

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val parent: ParentDto
)

data class ParentDto(
    val id: Int,
    val nom: String,
    val prenom: String,
    val email: String,
    val civilite: String? = null
)