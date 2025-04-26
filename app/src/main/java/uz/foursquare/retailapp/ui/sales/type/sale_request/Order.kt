package uz.foursquare.retailapp.ui.sales.type.sale_request


import com.google.gson.annotations.SerializedName

data class Order(
    @SerializedName("discount")
    val discount: Double,
    @SerializedName("product_id")
    val productId: String,
    @SerializedName("quantity")
    val quantity: Int
)