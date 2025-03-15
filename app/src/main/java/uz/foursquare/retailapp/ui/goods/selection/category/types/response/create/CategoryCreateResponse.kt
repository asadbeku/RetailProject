package uz.foursquare.retailapp.ui.goods.selection.category.types.response.create


import com.google.gson.annotations.SerializedName

data class CategoryCreateResponse(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("store")
    val store: Store,
    @SerializedName("updated_at")
    val updatedAt: String
)