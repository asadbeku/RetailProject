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
import uz.foursquare.retailapp.utils.toGoodTypeList
import javax.inject.Inject

class GoodsRepository @Inject constructor(
    private val client: HttpClient,
    private val apiService: ApiService,
    private val sharedPrefs: SharedPrefsManager
) {
    private val goodsUrl = "${apiService.baseUrl}/products"

    suspend fun getGoods(): Result<Unit> {
        return runCatching {
            val response = client.get(goodsUrl) {
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer ${sharedPrefs.getToken()}")
            }

            if (response.status.isSuccess()) {
                response.body<GoodsTypeResponse>().toGoodTypeList()
            } else {
                throw Exception("Failed to fetch goods: ${response.status}")
            }
        }
    }
}