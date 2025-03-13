package uz.foursquare.retailapp.ui.auth.login.view_model

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import uz.foursquare.retailapp.network.ApiService
import uz.foursquare.retailapp.ui.auth.login.type.LoginRequest
import uz.foursquare.retailapp.ui.auth.login.type.LoginResponse
import uz.foursquare.retailapp.utils.SharedPrefsManager
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val client: HttpClient,
    private val apiService: ApiService
) {
    suspend fun login(phoneNumber: String, password: String): Result<LoginResponse> {
        return try {
            val response: HttpResponse = client.post("${apiService.baseUrl}/auth/login") {
                contentType(ContentType.Application.Json) // ✅ Ensure Content-Type is set
                setBody(LoginRequest(phoneNumber, password))
            }

            if (response.status.isSuccess()) {
                val result: LoginResponse = response.body()
                Result.success(result)
            } else {
                val errorBody = response.bodyAsText()
                Result.failure(Exception("Login failed: ${response.status}, $errorBody"))
            }
        } catch (e: Exception) {
            Log.e("LoginRepository", "Login failed", e)
            Result.failure(e)
        }
    }
}