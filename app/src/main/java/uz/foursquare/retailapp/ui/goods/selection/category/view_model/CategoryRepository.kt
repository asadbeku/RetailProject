package uz.foursquare.retailapp.ui.goods.selection.category.view_model

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import com.google.gson.JsonObject
import io.ktor.client.request.get
import uz.foursquare.retailapp.network.ApiService
import uz.foursquare.retailapp.ui.goods.selection.category.types.CategoryType
import uz.foursquare.retailapp.ui.goods.selection.category.types.response.create.CategoryCreateResponse
import uz.foursquare.retailapp.ui.goods.selection.category.types.response.get.CategoryGetResponse
import uz.foursquare.retailapp.ui.goods.selection.category.types.response.get.CategoryGetResponseItem
import uz.foursquare.retailapp.utils.SharedPrefsManager
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val apiService: ApiService,
    private val client: HttpClient,
    private val sharedPrefsManager: SharedPrefsManager
) {
    suspend fun getCategories(): Result<List<CategoryType>> = runCatching {
        val response = client.get("${apiService.baseUrl}/categories") {
            header("Authorization", "Bearer ${sharedPrefsManager.getToken()}")
            contentType(ContentType.Application.Json)
        }

        if (!response.status.isSuccess()) {
            throw Exception("Failed to fetch categories: ${response.status}")
        }

        val result: CategoryGetResponse = response.body()
        result.map { it.toCategoryType() }.sortedBy { it.name }
    }

    suspend fun addCategory(categoryName: String): Result<String> = runCatching {
        val response = client.post("${apiService.baseUrl}/categories") {
            contentType(ContentType.Application.Json)
            header("Authorization", "Bearer ${sharedPrefsManager.getToken()}")
            setBody(JsonObject().apply { addProperty("name", categoryName) })
        }

        if (!response.status.isSuccess()) {
            throw Exception("Failed to add category: ${response.status}")
        }

        "Success"
    }
}

private fun CategoryGetResponseItem.toCategoryType(): CategoryType {
    return CategoryType(
        id = this.id,
        name = this.name,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        productsCount = this.products.size
    )
}