package uz.foursquare.retailapp.ui.goods.selection.brand.view_model

import com.google.firebase.perf.FirebasePerformance
import com.google.gson.JsonObject
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import uz.foursquare.retailapp.network.ApiService
import uz.foursquare.retailapp.ui.goods.selection.brand.types.BrandType
import uz.foursquare.retailapp.ui.goods.selection.brand.types.response.BrandGetResponse
import uz.foursquare.retailapp.ui.goods.selection.brand.types.response.BrandGetResponseItem
import uz.foursquare.retailapp.utils.SharedPrefsManager
import javax.inject.Inject

class BrandRepository @Inject constructor(
    private val apiService: ApiService,
    private val client: HttpClient
) {

    suspend fun getBrands(): Result<List<BrandType>> = runCatching {
        val trace = FirebasePerformance.getInstance().newTrace("get_brand_response")
        trace.start()
        val response = client.get("${apiService.baseUrl}/brands") {
            contentType(ContentType.Application.Json)
        }

        if (!response.status.isSuccess()) {
            trace.stop()
            throw Exception("Failed to fetch brands: ${response.status}")
        }
        trace.stop()
        val result: BrandGetResponse = response.body()
        result.map { it.toBrandType() }
    }

    suspend fun addBrand(brandName: String): Result<Unit> = runCatching {
        val trace = FirebasePerformance.getInstance().newTrace("add_brand_response")
        trace.start()
        val response = client.post("${apiService.baseUrl}/brands") {
            contentType(ContentType.Application.Json)
            setBody(JsonObject().apply { addProperty("name", brandName) })
        }

        if (!response.status.isSuccess()) {
            trace.stop()
            throw Exception("Failed to add brand: ${response.status}")
        }
        trace.stop()
    }
}

// Extension function to convert API response model to domain model
private fun BrandGetResponseItem.toBrandType(): BrandType {
    return BrandType(
        id = this.id,
        name = this.name,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}
