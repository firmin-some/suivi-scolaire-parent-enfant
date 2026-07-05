package bf.ujkz.suiviscolaireparent.network

data class NotificationDto(
    val id: Int,
    val titre: String,
    val message: String,
    val lu: Boolean,
    val date: String
)