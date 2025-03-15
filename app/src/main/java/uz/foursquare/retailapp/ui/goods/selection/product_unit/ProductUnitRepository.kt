package uz.foursquare.retailapp.ui.goods.selection.product_unit

import uz.foursquare.retailapp.ui.goods.selection.product_unit.type.ProductUnitType
import javax.inject.Inject

class ProductUnitRepository @Inject constructor() {

    val list = listOf(
        ProductUnitType(1, "dona"),
        ProductUnitType(2, "kg"),
        ProductUnitType(3, "tonna"),
        ProductUnitType(4, "litr"),
        ProductUnitType(5, "metr")
    )

    suspend fun getProductUnits(): Result<List<ProductUnitType>> {
        return Result.success(list)
    }

    suspend fun addProductUnit(unitName: String): Result<String> {
        return Result.success("Successfully added!")
    }
}