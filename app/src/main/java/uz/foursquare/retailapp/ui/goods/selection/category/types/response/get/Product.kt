package uz.foursquare.retailapp.ui.goods.selection.category.types.response.get


import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("barcode")
    val barcode: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("discount_price")
    val discountPrice: String,
    @SerializedName("expiration_date")
    val expirationDate: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("purchase_price")
    val purchasePrice: String,
    @SerializedName("purchase_price_usd")
    val purchasePriceUsd: String,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("sale_price")
    val salePrice: String,
    @SerializedName("sale_price_usd")
    val salePriceUsd: String,
    @SerializedName("sku")
    val sku: String,
    @SerializedName("unit")
    val unit: String,
    @SerializedName("updated_at")
    val updatedAt: String
)