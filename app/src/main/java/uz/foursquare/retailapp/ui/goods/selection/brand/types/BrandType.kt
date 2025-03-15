package uz.foursquare.retailapp.ui.goods.selection.brand.types

data class BrandType(
    val id: String,
    val name: String,
    val createdAt: String,
    val updatedAt: String,
    val logoUrl: String? = null,
)