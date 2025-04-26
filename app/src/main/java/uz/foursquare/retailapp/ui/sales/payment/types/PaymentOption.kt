package uz.foursquare.retailapp.ui.sales.payment.types

data class PaymentOption(
    val method: PaymentMethod,
    val displayName: String,
    val isSelected: Boolean
)
