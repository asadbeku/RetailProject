package uz.foursquare.retailapp.ui.goods.selection

import uz.foursquare.retailapp.ui.goods.selection.brand.types.BrandType
import uz.foursquare.retailapp.ui.goods.selection.category.types.CategoryType
import uz.foursquare.retailapp.ui.goods.selection.suppliers.types.SupplierType

sealed class SelectionList {
    data class Categories(val items: List<CategoryType>) : SelectionList()
    data class Suppliers(val items: List<SupplierType>) : SelectionList()
    data class Brands(val items: List<BrandType>) : SelectionList()
    object Unknown : SelectionList()
}