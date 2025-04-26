package uz.foursquare.retailapp.ui.sales.search_product.view_model

import com.google.firebase.perf.FirebasePerformance
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import uz.foursquare.retailapp.network.ApiService
import uz.foursquare.retailapp.ui.goods.goods_screen.types.GoodType
import uz.foursquare.retailapp.ui.goods.goods_screen.types.response.GoodsTypeResponse
import uz.foursquare.retailapp.utils.toGoodTypeList
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val client: HttpClient,
    private val apiService: ApiService
) {

    suspend fun searchByNames(names: List<String>): Result<List<GoodType>> = coroutineScope {
        val trace = FirebasePerformance.getInstance().newTrace("search_product_response")
        trace.start()
        if (names.isEmpty()) return@coroutineScope Result.success(emptyList())

        val deferredResults = names.map { name ->
            async {
                client.get("${apiService.baseUrl}/products") {
                    url { parameters.append("n", name) }
                    contentType(ContentType.Application.Json)
                }
            }
        }

        val responses = deferredResults.awaitAll() // Wait for all responses

        val productList = responses
            .filter { it.status.isSuccess() }
            .flatMap { it.body<GoodsTypeResponse>().toGoodTypeList() } // Extract list
        trace.stop()
        return@coroutineScope Result.success(productList)
    }
}

private val list = listOf(
    GoodType(
        id = "dds",
        name = "Fudbolka",
        count = 5,
        salePrice = 15000.0,
        purchasePrice = 14000.0,
        purchasePriceUSD = 1.0,
        salePriceUSD = 1.1,
        uniteType = "Dona",
        barcode = "312313213231",
    ), GoodType(
        id = "dds",
        name = "Shim",
        count = 5,
        salePrice = 2000000.0,
        purchasePrice = 14000.0,
        purchasePriceUSD = 1.0,
        salePriceUSD = 1.1,
        uniteType = "Dona",
        barcode = "312313213231",
    ), GoodType(
        id = "dds",
        name = "Oyoq kiyim",
        count = 100,
        salePrice = 1580000.0,
        purchasePrice = 14000.0,
        purchasePriceUSD = 1.0,
        salePriceUSD = 1.1,
        uniteType = "Dona",
        barcode = "312313213231",
    ), GoodType(
        id = "dds",
        name = "Krassovka hello Brand",
        count = 30,
        salePrice = 55602100.0,
        purchasePrice = 14000.0,
        purchasePriceUSD = 1.0,
        salePriceUSD = 1.1,
        uniteType = "Dona",
        barcode = "312313213231",
    )
)