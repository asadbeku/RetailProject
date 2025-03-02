package uz.foursquare.retailapp.network.auth_response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val brithday: String,
    val firstName: String,
    val id: String,
    val lastName: String,
    val phonenumber: String,
    val role: String,
    val status: String
)