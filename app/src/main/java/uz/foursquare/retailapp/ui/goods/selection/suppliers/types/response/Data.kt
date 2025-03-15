package uz.foursquare.retailapp.ui.goods.selection.suppliers.types.response


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("balance")
    val balance: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("debt_value")
    val debtValue: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("phone_numbers")
    val phoneNumbers: String?,
    @SerializedName("total_paid_purchase_price")
    val totalPaidPurchasePrice: String,
    @SerializedName("total_purchase_measurement_value")
    val totalPurchaseMeasurementValue: String,
    @SerializedName("total_purchase_price")
    val totalPurchasePrice: String,
    @SerializedName("updated_at")
    val updatedAt: String
)