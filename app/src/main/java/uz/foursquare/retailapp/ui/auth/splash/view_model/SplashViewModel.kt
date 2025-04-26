package uz.foursquare.retailapp.ui.auth.splash.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: SplashRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState = _authState.asStateFlow()

    init {
        checkAuth()
    }

    fun checkAuth() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val isAuthorized = repository.refreshToken()
                _authState.value = if (isAuthorized) {
                    AuthState.Authorized
                } else {
                    AuthState.Unauthorized
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class AuthState {
    object Loading : AuthState()
    object Authorized : AuthState()
    object Unauthorized : AuthState()
    data class Error(val message: String) : AuthState()
}