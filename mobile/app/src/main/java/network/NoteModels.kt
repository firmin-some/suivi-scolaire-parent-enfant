package bf.ujkz.suiviscolaireparent.network

data class NoteDto(
    val type: String,
    val valeur: Double,
    val coefficient: Int,
    val date: String
)

data class MatiereNotesDto(
    val matiere: String,
    val notes: List<NoteDto>,
    val moyenne: Double
)

data class NotesTrimestreDto(
    val trimestre: Int,
    val matieres: List<MatiereNotesDto>
)