package uz.foursquare.retailapp.ui.sales.transaction.customer.type

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CustomerType(
    val id: String,
    val name: String,
    val phoneNumber: String,
    val credit: Double,
    val orders: List<String>
) : Parcelable