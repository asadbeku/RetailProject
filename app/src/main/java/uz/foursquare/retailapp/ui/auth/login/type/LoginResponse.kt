package uz.foursquare.retailapp.ui.auth.login.type


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("token")
    val token: String
)