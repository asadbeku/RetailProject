package uz.foursquare.retailapp.ui.goods.goods_screen.types

data class GoodType(
    val id: String,
    val name: String,
    val count: Int,
    val salePrice: Double,
    val purchasePrice: Double,
    val purchasePriceUSD: Double,
    val salePriceUSD: Double,
    val uniteType: String,
    val barcode: String
)