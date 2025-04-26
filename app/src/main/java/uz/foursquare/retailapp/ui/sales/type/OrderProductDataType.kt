package uz.foursquare.retailapp.ui.sales.type

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderProductDataType(
    val productId: String,
    val quantity: Int,
    val discount: Double
) : Parcelable