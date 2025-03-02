package uz.foursquare.retailapp.ui.auth.login.type

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val phonenumber: String,
    val password: String
)
