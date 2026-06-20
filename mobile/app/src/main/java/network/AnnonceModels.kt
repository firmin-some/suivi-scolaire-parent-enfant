package bf.ujkz.suiviscolaireparent.network

data class AnnonceDto(
    val id: Int,
    val titre: String,
    val contenu: String,
    val type: String,
    val date: String
)