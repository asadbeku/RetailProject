package uz.foursquare.retailapp.ui.goods.selection.brand.types.response


import com.google.gson.annotations.SerializedName

data class BrandGetResponseItem(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("description")
    val description: Any?,
    @SerializedName("id")
    val id: String,
    @SerializedName("logoUrl")
    val logoUrl: Any?,
    @SerializedName("name")
    val name: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)