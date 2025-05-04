package uz.foursquare.retailapp.ui.sales.type

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import uz.foursquare.retailapp.ui.sales.transaction.customer.type.CustomerType

@Parcelize
data class OrderDataType(
    val transactionId: String,
    val transactionNumber: String = "",
    val products: List<OrderProductDataType>,
    val totalPrice: Double,
    val description: String,
    val customer: CustomerType?
) : Parcelable