package uz.foursquare.retailapp.network

import uz.foursquare.retailapp.network.auth_response.AuthLoginResponse

interface ApiService {
    suspend fun login(phoneNumber: String, password: String): AuthLoginResponse
}