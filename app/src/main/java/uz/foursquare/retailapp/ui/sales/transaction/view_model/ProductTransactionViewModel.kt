package uz.foursquare.retailapp.ui.sales.transaction.view_model

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uz.foursquare.retailapp.ui.sales.transaction.customer.type.CustomerType
import uz.foursquare.retailapp.ui.sales.type.OrderDataType
import javax.inject.Inject

@HiltViewModel
class ProductTransactionViewModel @Inject constructor(
    private val repository: ProductTransactionRepository
) : ViewModel() {

    private var _selectedCustomer = MutableStateFlow<CustomerType?>(null)
    val selectedCustomer: StateFlow<CustomerType?> = _selectedCustomer.asStateFlow()

    private var _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()

    private var _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var _totalOrderPrice = MutableStateFlow(0.0)
    val totalOrderPrice: StateFlow<Double> = _totalOrderPrice.asStateFlow()

    private var _discountedPrice = MutableStateFlow(0.0)
    val discountedPrice: StateFlow<Double> = _discountedPrice.asStateFlow()

    private var _description = MutableStateFlow("")
    val description: StateFlow<String> = _description.asStateFlow()

    private var _generalDiscountRate = MutableStateFlow(0.0)
    val generalDiscountRate: StateFlow<Double> = _generalDiscountRate.asStateFlow()

    private var _orderData = MutableStateFlow<OrderDataType?>(null)
//    val orderData: StateFlow<OrderDataType?> = _orderData.asStateFlow()

    private var _transactionNumber = MutableStateFlow<String?>(null)
    val transactionNumber: StateFlow<String?> = _transactionNumber.asStateFlow()

    val seller = MutableStateFlow<String>("")

    private val scope = CoroutineExceptionHandler { _, throwable ->
        _errorMessage.value = throwable.message ?: "Unknown error"
        _isLoading.value = false
    }

    fun setTransactionNumber(number: String) {
        _transactionNumber.value = number
    }

    fun setSelectedCustomer(customer: CustomerType?) {
        _selectedCustomer.value = customer
    }

    fun setDiscountedPrice(price: Double) {
        _discountedPrice.value = price
    }

    fun setDescription(description: String) {
        _description.value = description
    }

    fun setGeneralDiscountRate(rate: Double) {
        _generalDiscountRate.value = rate
    }

    init {
        getSeller()
    }

    fun setOrderData(orderData: OrderDataType) {
        _orderData.value = orderData

        setTotalOrderPrice(orderData.totalPrice)
    }

    fun getOrderData(): OrderDataType? {
        return _orderData.value?.copy(
            customer = selectedCustomer.value,
            discountSum = discountedPrice.value,
            description = description.value
        )
    }

    private fun setTotalOrderPrice(price: Double) {
        _totalOrderPrice.value = price
    }


    fun getSeller() {
        viewModelScope.launch(scope + Dispatchers.IO) {
            _isLoading.value = true
            repository.getSeller()
                .onSuccess {
                    seller.value = it
                    _isLoading.value = false
                }
                .onFailure {
                    _errorMessage.value = it.message ?: "Unknown error"
                    _isLoading.value = false
                }
        }
    }


}