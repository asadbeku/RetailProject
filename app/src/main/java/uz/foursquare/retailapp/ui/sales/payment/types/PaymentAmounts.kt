package uz.foursquare.retailapp.ui.sales.payment.types

data class PaymentAmounts(
    val cash: Float = 0.0f,
    val card: Float = 0.0f,
    val credit: Float = 0.0f
) {
    val totalPaid get() = cash + card + credit
}
