package uz.foursquare.retailapp.ui.goods.fake

import uz.foursquare.retailapp.ui.goods.goods_screen.types.GoodType
import uz.foursquare.retailapp.ui.goods.goods_screen.view_model.GoodsRepository

class FakeGoodsRepository {
    suspend fun getGoods(): Result<List<GoodType>> {
        return Result.success(
            listOf(
                GoodType(
                    "1g",
                    "Fudbolka fake",
                    50,
                    150000.0,
                    100000.0,
                    9.5,
                    10.0,
                    "unit",
                    "123131"
                )
            )
        )
    }
}
