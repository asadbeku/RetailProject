package uz.foursquare.retailapp.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.plugin
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.cookies
import io.ktor.http.isSuccess

suspend fun refreshToken(client: HttpClient): Boolean {
    return try {
        val response: HttpResponse = client.post("https://dev.back.kipavtomatika.uz/auth/refresh"){
            contentType(ContentType.Application.Json)
        }
        response.status.isSuccess()
    } catch (e: Exception) {
        false
    }
}