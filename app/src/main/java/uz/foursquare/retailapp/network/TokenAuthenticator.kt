package uz.foursquare.retailapp.network

interface TokenAuthenticator {
    suspend fun refreshToken(): Boolean
}