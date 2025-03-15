package uz.foursquare.retailapp.ui.goods.selection.suppliers.types.response


import com.google.gson.annotations.SerializedName

data class SuppliersGetType(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("skip")
    val skip: Int,
    @SerializedName("take")
    val take: Int,
    @SerializedName("total")
    val total: Int
)