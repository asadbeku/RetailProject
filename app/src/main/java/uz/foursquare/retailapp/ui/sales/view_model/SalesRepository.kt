package uz.foursquare.retailapp.ui.sales.view_model

import com.google.firebase.perf.FirebasePerformance
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import uz.foursquare.retailapp.network.ApiService
import uz.foursquare.retailapp.ui.goods.goods_screen.types.GoodType
import uz.foursquare.retailapp.ui.goods.goods_screen.types.response.ProductData
import uz.foursquare.retailapp.ui.sales.type.new_sale.NewSaleResponseType
import uz.foursquare.retailapp.utils.convertToGoodType
import javax.inject.Inject

class SalesRepository @Inject constructor(
    private val client: HttpClient,
    private val apiService: ApiService
) {

    suspend fun getProductById(productId: String?): Result<GoodType> = coroutineScope {
        val trace = FirebasePerformance.getInstance().newTrace("get_product_by_id_response")
        trace.start()
        if (productId.isNullOrEmpty()) throw Exception("Product id is null or empty")

        val deferredResult = async {
            client.get("${apiService.baseUrl}/products/$productId") {
                contentType(ContentType.Application.Json)
            }
        }

        val response = deferredResult.await()
        trace.stop()
        return@coroutineScope Result.success(response.body<ProductData>().convertToGoodType())
    }

    suspend fun getSaleId(): Result<NewSaleResponseType> = coroutineScope {
        val trace = FirebasePerformance.getInstance().newTrace("create_new_sale")
        trace.start()

        val deferredResult = async {
            client.get("${apiService.baseUrl}/sales/new") {
                contentType(ContentType.Application.Json)
            }
        }

        val response = deferredResult.await()
        trace.stop()
        return@coroutineScope Result.success(response.body())
    }
}