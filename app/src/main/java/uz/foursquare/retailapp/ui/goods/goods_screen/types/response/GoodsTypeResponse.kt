package uz.foursquare.retailapp.ui.goods.goods_screen.types.response


import com.google.gson.annotations.SerializedName

data class GoodsTypeResponse(
    @SerializedName("data")
    val `data`: List<ProductData>,
    @SerializedName("skip")
    val skip: Int,
    @SerializedName("take")
    val take: String,
    @SerializedName("total")
    val total: Int
)