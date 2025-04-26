package uz.foursquare.retailapp.ui.sales.transaction.customer.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.foursquare.retailapp.ui.sales.transaction.customer.type.CustomerType
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(
    private val repository: CustomerRepository
) : ViewModel() {

    private val _customers: MutableStateFlow<List<CustomerType>> = MutableStateFlow(emptyList())
    val customers: StateFlow<List<CustomerType>> = _customers.asStateFlow()

    private val _errorMessage = MutableStateFlow<String>("")
    val errorMessage = _errorMessage.asSharedFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isCompletedTask = MutableStateFlow(false)
    val isCompletedTask: StateFlow<Boolean> = _isCompletedTask.asStateFlow()

    private val scope = CoroutineExceptionHandler { _, throwable ->
        _errorMessage.value = throwable.message.toString()
        _isLoading.value = false
    }

    init {
        getCustomers()
    }

    fun addCustomer(customer: CustomerType) {
        viewModelScope.launch {
            repository.addCustomer(customer)
        }
    }

    fun editCustomer(customer: CustomerType) {
        viewModelScope.launch {
            repository.editCustomer(customer)
        }
    }

    fun archiveCustomer(customerId: String) {
        viewModelScope.launch {
            repository.archiveCustomer(customerId)
        }
    }

    fun searchCustomer(query: String) {
        viewModelScope.launch {
            repository.searchCustomer(query)
        }
    }

    fun getCustomerByPosition(position: Int): CustomerType {
        return _customers.value[position]
    }

    fun getCustomers() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getCustomers()
            if (result.isSuccess) {
                _customers.value = result.getOrDefault(emptyList())
                _isLoading.value = false
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message.toString()
                _isLoading.value = false
            }
        }
    }


}