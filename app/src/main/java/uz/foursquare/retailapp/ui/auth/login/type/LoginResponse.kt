package uz.foursquare.retailapp.ui.auth.login.type

import com.google.gson.annotations.SerializedName
import uz.foursquare.retailapp.ui.auth.login.User

data class LoginResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("user")
    val user: User
)