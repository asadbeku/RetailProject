package uz.foursquare.retailapp.network

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiService @Inject constructor(
    val baseUrl: String
)