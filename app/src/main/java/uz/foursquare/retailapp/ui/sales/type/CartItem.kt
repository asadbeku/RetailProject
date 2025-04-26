package uz.foursquare.retailapp.ui.sales.type

import uz.foursquare.retailapp.ui.goods.goods_screen.types.GoodType

data class CartItem(
    val product: GoodType,
    val quantity: Int,
    val priceWithDiscount: Double
)