package uz.foursquare.retailapp.ui.auth.login.view_model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel : ViewModel() {

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    // Functions to update the state
    fun updatePhoneNumber(newPhoneNumber: String) {
        _phoneNumber.value = newPhoneNumber
    }

    fun updatePassword(newPassword: String) {
        _password.value = newPassword
    }

    fun login(onSuccess: () -> Unit) {
        if ((_phoneNumber.value.isNotEmpty()&& _phoneNumber.value.length==13) && _password.value.isNotEmpty()) {
            println("Logging in with Phone: ${_phoneNumber.value}, Password: ${_password.value}")
            onSuccess()
        } else {
            println("Phone number or password is empty")
        }
    }

}