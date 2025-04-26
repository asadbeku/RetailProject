package uz.foursquare.retailapp.ui.sales.type.sale_request


import com.google.gson.annotations.SerializedName

data class Payment(
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("method")
    val method: String
)