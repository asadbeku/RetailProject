package uz.foursquare.retailapp.ui.sales.type.sale_request


import com.google.gson.annotations.SerializedName

data class SaleRequestType(
    @SerializedName("client_id")
    val clientId: String,
    @SerializedName("orders")
    val orders: List<Order>,
    @SerializedName("payments")
    val payments: List<Payment>,
    @SerializedName("sale_id")
    val saleId: String
)