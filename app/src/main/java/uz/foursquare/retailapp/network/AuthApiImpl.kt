package uz.foursquare.retailapp.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import uz.foursquare.retailapp.network.auth_response.AuthLoginResponse
import uz.foursquare.retailapp.ui.auth.login.type.LoginRequest
import javax.inject.Inject

class AuthApiImpl @Inject constructor(private val client: HttpClient) : ApiService {
    override suspend fun login(phoneNumber: String, password: String): AuthLoginResponse {
        return client.post("https://dev.back.kipavtomatika.uz/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(phoneNumber, password))
        }.body()
    }
}