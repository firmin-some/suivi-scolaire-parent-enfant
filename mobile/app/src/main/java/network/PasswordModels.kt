package bf.ujkz.suiviscolaireparent.network

data class UpdatePasswordRequest(
    val ancien_mot_de_passe: String,
    val nouveau_mot_de_passe: String,
    val nouveau_mot_de_passe_confirmation: String
)