package uz.foursquare.retailapp.ui.auth.login.view_model

import uz.foursquare.retailapp.network.auth_response.AuthLoginResponse

interface LoginRepository {
    suspend fun login(phone: String, password: String): AuthLoginResponse
}