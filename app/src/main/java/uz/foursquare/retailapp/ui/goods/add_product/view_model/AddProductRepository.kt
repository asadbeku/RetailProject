package uz.foursquare.retailapp.ui.goods.add_product.view_model

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import uz.foursquare.retailapp.network.ApiService
import uz.foursquare.retailapp.ui.goods.add_product.type.request.GoodRequest
import uz.foursquare.retailapp.ui.goods.add_product.type.response.AddProductResponse
import uz.foursquare.retailapp.utils.SharedPrefsManager
import javax.inject.Inject

class AddProductRepository @Inject constructor(
    private val client: HttpClient,
    private val apiService: ApiService,
    private val sharedPrefs: SharedPrefsManager
) {
    private val goodsUrl = "${apiService.baseUrl}/products"

    suspend fun addProduct(product: GoodRequest): Result<AddProductResponse> {
        return runCatching {
            val response = client.post(goodsUrl) {
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer ${sharedPrefs.getToken()}")
                setBody(product)
            }

            if (response.status.isSuccess()) {
                response.body<AddProductResponse>()
            } else {
                throw Exception("Failed to fetch goods: ${response.status}")
            }
        }
    }

}