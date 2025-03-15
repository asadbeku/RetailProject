package uz.foursquare.retailapp.ui.goods.selection.category.types.response.get


import com.google.gson.annotations.SerializedName

data class CategoryGetResponseItem(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("products")
    val products: List<Product>,
    @SerializedName("updated_at")
    val updatedAt: String
)