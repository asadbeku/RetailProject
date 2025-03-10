package uz.foursquare.retailapp.ui.goods.goods_screen.view_model

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.http.parameters
import uz.foursquare.retailapp.network.ApiService
import uz.foursquare.retailapp.ui.goods.goods_screen.types.GoodType
import uz.foursquare.retailapp.ui.goods.goods_screen.types.response.GoodsTypeResponse
import uz.foursquare.retailapp.utils.SharedPrefsManager
import javax.inject.Inject

class GoodsRepository @Inject constructor(
    private val client: HttpClient,
    private val apiService: ApiService,
    private val sharedPrefs: SharedPrefsManager
) {
    suspend fun getGoods(): Result<List<GoodType>> {
        return try {
            val response = client.get("${apiService.baseUrl}/products") {
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer ${sharedPrefs.getToken()}")
            }

            if (response.status.isSuccess()) {
                Result.success(convertToGoodType(response.body()))
            } else {
                Result.failure(Exception())
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}

private fun convertToGoodType(goodsTypeResponse: GoodsTypeResponse): List<GoodType> {
    val list = mutableListOf<GoodType>()
    goodsTypeResponse.data.map {
        list.add(
            GoodType(
                id = it.id,
                name = it.name,
                salePrice = it.salePrice.toDouble().toLong(),  // Convert properly
                purchasePrice = it.purchasePrice.toDouble().toLong(),
                purchasePriceUSD = it.purchasePriceUsd.toDouble().toLong(),
                salePriceUSD = it.salePriceUsd.toDouble().toLong(),
                count = it.quantity,
                uniteType = it.unit,
                barcode = it.barcode,
            )
        )
    }
    return list
}
