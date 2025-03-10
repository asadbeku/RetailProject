package uz.foursquare.retailapp.ui.goods.goods_screen.types

data class GoodType(
    val id: String,
    val name: String,
    val count: Int,
    val salePrice: Long,
    val purchasePrice: Long,
    val purchasePriceUSD: Long,
    val salePriceUSD: Long,
    val uniteType: String,
    val barcode: String
)