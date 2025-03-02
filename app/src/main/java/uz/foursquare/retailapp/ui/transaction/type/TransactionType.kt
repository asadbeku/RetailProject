package uz.foursquare.retailapp.ui.transaction.type

data class TransactionType(
    val id: Int,
    val salesNumber: Long,
    val paymentType: String,
    val paymentSum: Long,
    val paymentDate: Long,
    val paymentStore: String
)
