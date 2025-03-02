package uz.foursquare.retailapp.ui.goods

data class GoodType(
    val id: Int,
    val name: String,
    val count: Int,
    val image: String,
    val price: Long,
    val uniteType: String,
    val barcode: String,
    val isActive: Boolean,
    val isAvailable: Boolean
)