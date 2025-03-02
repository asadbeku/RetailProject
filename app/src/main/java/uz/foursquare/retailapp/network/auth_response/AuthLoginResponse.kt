package uz.foursquare.retailapp.network.auth_response

import kotlinx.serialization.SerialName


data class AuthLoginResponse(
    @SerialName("data")
    val `data`: Data,
    @SerialName("token")
    val token: String
)