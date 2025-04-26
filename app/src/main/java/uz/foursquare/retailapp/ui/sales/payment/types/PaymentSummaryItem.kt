package uz.foursquare.retailapp.ui.sales.payment.types

data class PaymentSummaryItem(
    val method: PaymentMethod,
    val displayName: String,
    val amount: Double
)
