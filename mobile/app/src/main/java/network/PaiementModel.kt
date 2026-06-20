package bf.ujkz.suiviscolaireparent.network

data class VersementDto(
    val id: Int,
    val date: String,
    val montant: Int,
    val mode_paiement: String?,
    val recu_url: String?
)

data class PaiementsDto(
    val montant_total_du: Int,
    val montant_paye: Int,
    val montant_restant: Int,
    val versements: List<VersementDto>
)
data class PaiementCreateRequest(
    val montant: Int,
    val mode_paiement: String
)