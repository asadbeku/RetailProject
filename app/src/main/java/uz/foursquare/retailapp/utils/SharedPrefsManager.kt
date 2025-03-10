package uz.foursquare.retailapp.utils

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

@Singleton
class SharedPrefsManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    fun saveToken(token: String) {
        sharedPreferences.edit() { putString("token", token) }
    }

    fun getToken(): String? {
        return sharedPreferences.getString("token", null)
    }

    fun clearToken() {
        sharedPreferences.edit { remove("token") }
    }
}