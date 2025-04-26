package uz.foursquare.retailapp.ui.auth.splash.view_model

import android.util.Log
import com.google.firebase.perf.FirebasePerformance
import io.ktor.client.HttpClient
import io.ktor.client.plugins.timeout
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import uz.foursquare.retailapp.utils.SharedPrefsManager
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class SplashRepository @Inject constructor(
    private val client: HttpClient,
    private val sharedPrefs: SharedPrefsManager
) {
    suspend fun refreshToken(): Boolean {
        val trace = FirebasePerformance.getInstance().newTrace("refresh_token_response")
        trace.start()
        return try {
            val accessToken = sharedPrefs.getAccessToken()
            val refreshToken = sharedPrefs.getRefreshToken()

            if (accessToken == null || refreshToken == null) {
                return false
            }

            val response = client.post("https://dev.back.kipavtomatika.uz/auth/refresh") {
                headers {
                    append(HttpHeaders.Cookie, "access_token=$accessToken")
                    append(HttpHeaders.Cookie, "refresh_token=$refreshToken")
                }

                timeout {
                    requestTimeoutMillis = 30_000
                    connectTimeoutMillis = 10_000
                }
            }

            response.status.isSuccess()
        } catch (e: Exception) {
            false
        } finally {
            trace.stop()
        }
    }
}
