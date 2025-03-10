package uz.foursquare.retailapp.ui.goods.goods_screen.types.response


import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("updated_at")
    val updatedAt: String
)