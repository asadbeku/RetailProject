package uz.foursquare.retailapp.ui.sales.type.new_sale


import com.google.gson.annotations.SerializedName

data class Cashier(
    @SerializedName("brithday")
    val brithday: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("phonenumber")
    val phonenumber: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("updated_at")
    val updatedAt: String
)