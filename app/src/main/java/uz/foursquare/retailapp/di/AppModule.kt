package uz.foursquare.retailapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import uz.foursquare.retailapp.network.ApiService
import uz.foursquare.retailapp.ui.auth.login.view_model.LoginRepository
import javax.inject.Singleton
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.gson.*

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO) { // ✅ Use CIO engine
            install(ContentNegotiation) {
                gson { // ✅ Use Gson instead of json(Json { ... })
                    setPrettyPrinting() // Optional: Formats JSON output
                    disableHtmlEscaping() // Optional: Prevents escaping special characters
                }
            }
        }
    }

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return ApiService("https://dev.back.kipavtomatika.uz") // Replace with actual URL
    }

    @Provides
    @Singleton
    fun provideLoginRepository(client: HttpClient, apiService: ApiService): LoginRepository {
        return LoginRepository(client, apiService)
    }
}