package uz.foursquare.retailapp.network.auth_response

import uz.foursquare.retailapp.network.ApiService
import uz.foursquare.retailapp.ui.auth.login.view_model.LoginRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val api: ApiService) : LoginRepository {
    override suspend fun login(phone: String, password: String): AuthLoginResponse {
        return api.login(phone, password)
    }
}