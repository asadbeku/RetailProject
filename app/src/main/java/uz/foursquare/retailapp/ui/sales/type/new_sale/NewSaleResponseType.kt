package uz.foursquare.retailapp.ui.sales.type.new_sale


import com.google.gson.annotations.SerializedName

data class NewSaleResponseType(
    @SerializedName("cashier")
    val cashier: Cashier,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("description")
    val description: Any?,
    @SerializedName("id")
    val id: String,
    @SerializedName("sale_number")
    val saleNumber: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("store")
    val store: Store,
    @SerializedName("total_price")
    val totalPrice: Int,
    @SerializedName("updated_at")
    val updatedAt: String
)