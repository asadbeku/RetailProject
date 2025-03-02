package uz.foursquare.retailapp.ui.auth.login.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uz.foursquare.retailapp.navigation.Graph
import uz.foursquare.retailapp.navigation.auth.AuthScreen
import uz.foursquare.retailapp.ui.auth.login.type.LoginResponse
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun updatePhoneNumber(newPhoneNumber: String) {
        _phoneNumber.value = newPhoneNumber
        validateInput()
    }

    fun updatePassword(newPassword: String) {
        _password.value = newPassword
        validateInput()
    }

    private fun validateInput() {
        val isPhoneValid = _phoneNumber.value.length == 13
        val isPasswordValid = _password.value.length >= 6
        _uiState.value = _uiState.value.copy(isLoginEnabled = isPhoneValid && isPasswordValid)
    }

    fun login(navController: NavController) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            Log.d("LoginViewModel", "phoneNumber: ${_phoneNumber.value}, password: ${_password.value}")
            val response = loginRepository.login(_phoneNumber.value, _password.value)
            Log.d("LoginViewModel", "response: $response")
            if (response.isSuccess) {
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = null)
                navController.navigate(Graph.MAIN) {
                    popUpTo(AuthScreen.Login.route) { inclusive = true }
                }
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = response.exceptionOrNull()?.message
                )
            }
        }
    }
}

data class LoginUiState(
    val isLoginEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
