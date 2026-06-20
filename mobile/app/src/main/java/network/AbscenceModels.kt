package bf.ujkz.suiviscolaireparent.network

data class AbsenceDto(
    val id: Int,
    val date: String,
    val motif: String?,
    val justifiee: Boolean
)