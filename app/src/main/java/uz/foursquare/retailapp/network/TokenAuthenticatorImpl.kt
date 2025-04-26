package uz.foursquare.retailapp.network

import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import uz.foursquare.retailapp.utils.SharedPrefsManager
import javax.inject.Inject

class TokenAuthenticatorImpl @Inject constructor(
    private val client: HttpClient,
    private val sharedPrefs: SharedPrefsManager
) : TokenAuthenticator {

    override suspend fun refreshToken(): Boolean {
        return try {
            val refreshToken = sharedPrefs.getRefreshToken()
            if (refreshToken.isNullOrEmpty()) {
                return false
            }

            val response = client.post("https://dev.back.kipavtomatika.uz/auth/refresh") {
                header(HttpHeaders.Authorization, "Bearer $refreshToken")
            }

            if (response.status.isSuccess()) {
                // Parse and save new tokens
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }
}