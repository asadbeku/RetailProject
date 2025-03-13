package uz.foursquare.retailapp.ui.auth.login.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import uz.foursquare.retailapp.navigation.Graph
import uz.foursquare.retailapp.navigation.auth.AuthScreen
import uz.foursquare.retailapp.ui.auth.login.type.LoginResponse
import uz.foursquare.retailapp.ui.auth.login.type.LoginUiState
import uz.foursquare.retailapp.utils.SharedPrefsManager
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val sharedPrefs: SharedPrefsManager
) : ViewModel() {

    private val _errorFlow = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val errorFlow = _errorFlow.asSharedFlow()

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
            val response = loginRepository.login(_phoneNumber.value, _password.value)

            if (response.isSuccess) {
                _uiState.value = _uiState.value.copy(isLoading = false)

                sharedPrefs.saveToken(response.getOrThrow().token)

                navController.navigate(Graph.MAIN) {
                    popUpTo(AuthScreen.Login.route) { inclusive = true }
                }
            } else {
                val exceptionMessage = response.exceptionOrNull()?.message.orEmpty()
                Log.d("LoginViewModel", "login: $exceptionMessage")

                val errorMessage = when {
                    exceptionMessage.contains("400") -> "Telefon raqami yoki parol noto‘g‘ri."
                    exceptionMessage.contains("401") -> "Login yoki parol xato, tekshirib qaytadan kiring!"
                    exceptionMessage.contains("403") -> "Ruxsat berilmadi."
                    exceptionMessage.contains("500") -> "Server xatosi. Keyinroq qayta urinib ko‘ring."
                    else -> "An unexpected error occurred."
                }

                _uiState.value = _uiState.value.copy(isLoading = false)
                _errorFlow.emit(errorMessage)
            }
        }
    }
}
