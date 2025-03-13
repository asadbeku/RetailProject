package uz.foursquare.retailapp.ui.goods.add_product.type.response


import com.google.gson.annotations.SerializedName

data class Store(
    @SerializedName("address")
    val address: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("contact")
    val contact: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("stir")
    val stir: String,
    @SerializedName("updated_at")
    val updatedAt: String
)