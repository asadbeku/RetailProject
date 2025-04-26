package uz.foursquare.retailapp.ui.sales.payment.view_model

import com.google.firebase.perf.FirebasePerformance
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import uz.foursquare.retailapp.network.ApiService
import uz.foursquare.retailapp.ui.sales.type.sale_request.SaleRequestType
import javax.inject.Inject

class PaymentRepository @Inject constructor(
    private val client: HttpClient,
    private val apiService: ApiService
) {
    suspend fun saveOrder(order: SaleRequestType): Result<Any> {
        val trace = FirebasePerformance.getInstance().newTrace("save_order")
        return try {
            trace.start()
            val response = client.patch("${apiService.baseUrl}/sales") {
                contentType(ContentType.Application.Json)
                setBody(order)
            }
            Result.success(response.body())
        } catch (e: Exception) {
            Result.failure(e)
        } finally {
            trace.stop()
        }
    }
}
