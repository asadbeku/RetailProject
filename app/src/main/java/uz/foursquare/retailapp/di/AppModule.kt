package uz.foursquare.retailapp.di

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import uz.foursquare.retailapp.network.ApiService
import uz.foursquare.retailapp.ui.auth.login.view_model.LoginRepository
import javax.inject.Singleton
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.AcceptAllCookiesStorage
import io.ktor.client.plugins.cookies.ConstantCookiesStorage
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.cookie
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.http.ContentType
import io.ktor.http.Cookie
import io.ktor.http.CookieEncoding
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpHeaders.Cookie
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers
import io.ktor.serialization.gson.*
import io.ktor.util.AttributeKey
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.runBlocking
import kotlinx.io.IOException
import uz.foursquare.retailapp.network.TokenAuthenticator
import uz.foursquare.retailapp.network.TokenAuthenticatorImpl
import uz.foursquare.retailapp.network.type.ErrorResponse
import uz.foursquare.retailapp.utils.SharedPrefsManager
import java.text.DateFormat
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    @AuthClient  // Create a qualifier annotation for this client
    fun provideAuthHttpClient(): HttpClient {
        return HttpClient(CIO) {
            // Basic configuration for auth client
            install(HttpTimeout) {
                requestTimeoutMillis = 30_000
                connectTimeoutMillis = 15_000
                socketTimeoutMillis = 30_000
            }

            install(ContentNegotiation) {
                gson {
                    setPrettyPrinting()
                    disableHtmlEscaping()
                    setDateFormat(DateFormat.LONG)
                }
            }

            install(DefaultRequest) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                header(HttpHeaders.Accept, ContentType.Application.Json)
            }

            // Basic logging
            install(Logging) {
                level = LogLevel.HEADERS
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("AuthKtorClient", message)
                    }
                }
            }
        }
    }

    @Provides
    @Singleton
    fun provideTokenAuthenticator(
        @AuthClient authClient: HttpClient,  // Use the auth client instead
        sharedPrefs: SharedPrefsManager
    ): TokenAuthenticator {
        return TokenAuthenticatorImpl(authClient, sharedPrefs)
    }

    @Provides
    @Singleton
    fun provideHttpClient(
        sharedPrefs: SharedPrefsManager,
        authenticator: TokenAuthenticator
    ): HttpClient {
        // Create a custom plugin to add cookies
        val authCookiesPlugin = createClientPlugin("AuthCookiesPlugin") {
            onRequest { request, _ ->
                // Add tokens as cookies if available
                sharedPrefs.getAccessToken()?.let { token ->
                    request.cookie("access_token", token)
                }
                sharedPrefs.getRefreshToken()?.let { token ->
                    request.cookie("refresh_token", token)
                }
            }
        }

        return HttpClient(CIO) {
            // Install the custom cookies plugin
            install(authCookiesPlugin)

            // Timeout configuration
            install(HttpTimeout) {
                requestTimeoutMillis = 30_000
                connectTimeoutMillis = 15_000
                socketTimeoutMillis = 30_000
            }

            // Content negotiation
            install(ContentNegotiation) {
                gson {
                    setPrettyPrinting()
                    disableHtmlEscaping()
                    setDateFormat(DateFormat.LONG)
                }
            }

            // Default headers
            install(DefaultRequest) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                header(HttpHeaders.Accept, ContentType.Application.Json)
            }

            // Logging
            install(Logging) {
                level = LogLevel.HEADERS
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("KtorClient", message)
                    }
                }
                filter { request ->
                    !request.url.host.contains("analytics", ignoreCase = true)
                }
            }

            // Retry policy
            install(HttpRequestRetry) {
                maxRetries = 3
                retryOnExceptionIf { request, cause ->
                    cause is IOException || cause is TimeoutCancellationException
                }
                delayMillis { retry -> retry * 1000L } // exponential backoff
            }

            // Auth interceptor
//            install(Auth) {
//                bearer {
//                    loadTokens {
//                        val accessToken = sharedPrefs.getAccessToken()
//                        BearerTokens(accessToken ?: "", sharedPrefs.getRefreshToken() ?: "")
//                    }
//                    refreshTokens {
//                        authenticator.refreshToken()
//                        val newToken = sharedPrefs.getAccessToken()
//                        BearerTokens(newToken ?: "", sharedPrefs.getRefreshToken() ?: "")
//                    }
//                }
//            }

            // Response validation
            HttpResponseValidator {
                validateResponse { response ->
                    val statusCode = response.status.value
                    when (statusCode) {
                        HttpStatusCode.Unauthorized.value -> {
                            if (!authenticator.refreshToken()) {
                                throw AuthenticationException("Refresh token failed")
                            }
                        }
                        in 400..599 -> {
                            val errorResponse = try {
                                response.body<ErrorResponse>()
                            } catch (e: Exception) {
                                null
                            }
                            when (response.status) {
                                HttpStatusCode.BadRequest -> throw BadRequestException(errorResponse?.message)
                                HttpStatusCode.Forbidden -> throw ForbiddenException(errorResponse?.message)
                                else -> throw ServerResponseException(
                                    response,
                                    errorResponse?.message.toString()
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    class AuthenticationException(message: String) : Exception(message)
    class BadRequestException(message: String?) : Exception(message)
    class ForbiddenException(message: String?) : Exception(message)

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return ApiService("https://dev.back.kipavtomatika.uz") // Replace with actual URL
    }

    @Provides
    @Singleton
    fun provideLoginRepository(
        client: HttpClient,
        apiService: ApiService,
        sharedPrefs: SharedPrefsManager
    ): LoginRepository {
        return LoginRepository(client, apiService, sharedPrefs)
    }

    @Provides
    @Singleton
    fun provideSharedPrefsManager(sharedPreferences: SharedPreferences): SharedPrefsManager {
        return SharedPrefsManager(sharedPreferences)
    }
}

// Create a qualifier annotation for the auth client
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthClient