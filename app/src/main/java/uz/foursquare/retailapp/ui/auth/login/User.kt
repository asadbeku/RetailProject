package uz.foursquare.retailapp.ui.auth.login


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("brithday")
    val brithday: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("phonenumber")
    val phonenumber: String,
    @SerializedName("status")
    val status: String
)