package uz.foursquare.retailapp.ui.goods.selection.suppliers.view_model

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
import uz.foursquare.retailapp.ui.goods.selection.suppliers.types.SupplierType
import uz.foursquare.retailapp.ui.goods.selection.suppliers.types.response.Data
import uz.foursquare.retailapp.ui.goods.selection.suppliers.types.response.SuppliersGetType
import uz.foursquare.retailapp.utils.SharedPrefsManager
import javax.inject.Inject

class SuppliersRepository @Inject constructor(
    private val apiService: ApiService,
    private val client: HttpClient
) {
    suspend fun getSuppliers(): Result<List<SupplierType>> = runCatching {
        val trace = FirebasePerformance.getInstance().newTrace("get_suppliers_response")
        trace.start()

        val response = client.get("${apiService.baseUrl}/suppliers") {
            contentType(ContentType.Application.Json)
        }

        if (!response.status.isSuccess()) {
            trace.stop()
            throw Exception("Failed to fetch suppliers: ${response.status}")
        }
        trace.stop()
        val result: SuppliersGetType = response.body()
        result.data.map { it.toSupplierType() }
    }

    suspend fun addSupplier(supplierName: String): Result<Unit> = runCatching {
        val trace = FirebasePerformance.getInstance().newTrace("add_supplier_response")
        trace.start()
        val response = client.post("${apiService.baseUrl}/suppliers") {
            contentType(ContentType.Application.Json)
            setBody(JsonObject().apply { addProperty("name", supplierName) })
        }

        if (!response.status.isSuccess()) {
            trace.stop()
            throw Exception("Failed to add supplier: ${response.status}")
        }
        trace.stop()
    }
}

private fun Data.toSupplierType(): SupplierType {
    return SupplierType(
        id = this.id,
        name = this.name,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        balance = this.balance.toDouble(),
        debitValue = this.debtValue.toDouble(),
        phoneNumber = this.phoneNumbers,
        totalPaidPurchasePrice = this.totalPaidPurchasePrice.toDouble(),
        totalPurchasePrice = this.totalPurchasePrice.toDouble(),
        totalPurchaseMeasurementValue = this.totalPurchaseMeasurementValue.toDouble()
    )
}
