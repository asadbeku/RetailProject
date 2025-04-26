package uz.foursquare.retailapp.utils

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

@Singleton
class SharedPrefsManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    companion object {
        private const val ACCESS_TOKEN_KEY = "access_token"
        private const val REFRESH_TOKEN_KEY = "refresh_token"
    }

    // Сохранение Access Token
    fun saveAccessToken(token: String) {
        sharedPreferences.edit().putString(ACCESS_TOKEN_KEY, token).apply()
    }

    // Сохранение Refresh Token
    fun saveRefreshToken(token: String) {
        sharedPreferences.edit().putString(REFRESH_TOKEN_KEY, token).apply()
    }

    // Получение Access Token
    fun getAccessToken(): String? {
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }

    // Получение Refresh Token
    fun getRefreshToken(): String? {
        return sharedPreferences.getString(REFRESH_TOKEN_KEY, null)
    }

    // Очистка Access Token
    fun clearAccessToken() {
        sharedPreferences.edit().remove(ACCESS_TOKEN_KEY).apply()
    }

    // Очистка Refresh Token
    fun clearRefreshToken() {
        sharedPreferences.edit().remove(REFRESH_TOKEN_KEY).apply()
    }

    // Очистка всех токенов
    fun clearAllTokens() {
        sharedPreferences.edit().remove(ACCESS_TOKEN_KEY).remove(REFRESH_TOKEN_KEY).apply()
    }
}
