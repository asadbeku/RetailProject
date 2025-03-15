package uz.foursquare.retailapp.ui.goods.selection.category.types

import uz.foursquare.retailapp.ui.goods.goods_screen.types.GoodType

data class CategoryType(
    val id: String,
    val name: String,
    val createdAt: String,
    val updatedAt: String,
    val productsCount: Int
)
