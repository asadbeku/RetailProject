package uz.foursquare.retailapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import uz.foursquare.retailapp.network.ApiService
import uz.foursquare.retailapp.network.AuthApiImpl
import uz.foursquare.retailapp.network.auth_response.AuthRepositoryImpl
import uz.foursquare.retailapp.ui.auth.login.view_model.LoginRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideAuthApi(client: HttpClient): ApiService {
        return AuthApiImpl(client)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(api: ApiService): LoginRepository {
        return AuthRepositoryImpl(api)
    }
}