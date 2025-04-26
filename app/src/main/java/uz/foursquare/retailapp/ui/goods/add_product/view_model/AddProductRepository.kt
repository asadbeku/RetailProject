package uz.foursquare.retailapp.ui.goods.add_product.view_model

import com.google.firebase.perf.FirebasePerformance
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
        val trace = FirebasePerformance.getInstance().newTrace("add_product_response")
        trace.start()
        return runCatching {
            val response = client.post(goodsUrl) {
                contentType(ContentType.Application.Json)
                setBody(product)
            }

            if (response.status.isSuccess()) {
                trace.stop()
                response.body<AddProductResponse>()
            } else {
                trace.stop()
                println(response)
                throw Exception("Failed to fetch goods: ${response.status}")
            }
        }
    }
}