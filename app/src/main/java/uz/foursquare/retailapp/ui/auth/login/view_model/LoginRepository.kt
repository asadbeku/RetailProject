package uz.foursquare.retailapp.ui.auth.login.view_model

import android.content.Context
import android.util.Log
import com.google.firebase.perf.FirebasePerformance
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.cookies.cookies
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import uz.foursquare.retailapp.network.ApiService
import uz.foursquare.retailapp.ui.auth.login.type.LoginRequest
import uz.foursquare.retailapp.ui.auth.login.type.LoginResponse
import uz.foursquare.retailapp.utils.SharedPrefsManager
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val client: HttpClient,
    private val apiService: ApiService,
    private val sharedPrefs: SharedPrefsManager
) {
    suspend fun login(phoneNumber: String, password: String): Result<LoginResponse> {
        val trace = FirebasePerformance.getInstance().newTrace("login")
        trace.start()
        return try {
            val response: HttpResponse = client.post("${apiService.baseUrl}/auth/login") {
                contentType(ContentType.Application.Json) // ✅ Ensure Content-Type is set
                setBody(LoginRequest(phoneNumber, password))
            }

            if (response.status.isSuccess()) {
                val setCookieHeaders = response.headers.getAll(HttpHeaders.SetCookie)
                setCookieHeaders?.forEach { cookie ->
                    if (cookie.contains("access_token")) {
                        val accessToken =
                            cookie.substringAfter("access_token=").substringBefore(";")
                        println("Access Token: $accessToken")
                        sharedPrefs.saveAccessToken(accessToken)
                    }
                    if (cookie.contains("refresh_token")) {
                        val refreshToken =
                            cookie.substringAfter("refresh_token=").substringBefore(";")
                        println("Refresh Token: $refreshToken")
                        sharedPrefs.saveRefreshToken(refreshToken)
                    }
                }

                val result: LoginResponse = response.body()
                Result.success(result)
            } else {
                val errorBody = response.bodyAsText()
                Result.failure(Exception("Login failed: ${response.status}, $errorBody"))
            }
        } catch (e: Exception) {
            Log.e("LoginRepository", "Login failed", e)
            Result.failure(e)
        } finally {
            trace.stop()
        }
    }
}