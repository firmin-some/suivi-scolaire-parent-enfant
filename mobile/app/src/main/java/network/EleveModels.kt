package bf.ujkz.suiviscolaireparent.network

data class EleveDto(
    val id: Int,
    val nom: String,
    val prenom: String,
    val photo_url: String?,
    val classe: String
)

data class NoteResumeDto(
    val matiere: String,
    val valeur: Double,
    val date: String
)

data class EleveDetailDto(
    val id: Int,
    val nom: String,
    val prenom: String,
    val photo_url: String?,
    val classe: String,
    val moyenne_generale: Double,
    val rang_classe: Int,
    val effectif_classe: Int,
    val dernieres_notes: List<NoteResumeDto>
)