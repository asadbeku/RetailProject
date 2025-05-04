package uz.foursquare.retailapp.ui.sales.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.foursquare.retailapp.ui.sales.type.CartItem
import uz.foursquare.retailapp.ui.sales.type.OrderDataType
import uz.foursquare.retailapp.ui.sales.type.OrderProductDataType
import javax.inject.Inject

@HiltViewModel
class SalesViewModel @Inject constructor(
    private val repository: SalesRepository
) : ViewModel() {

    private val _goods = MutableStateFlow<List<CartItem>>(emptyList())
    val goods: StateFlow<List<CartItem>> = _goods.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _saleId = MutableStateFlow<String?>(null)
    val saleId: StateFlow<String?> = _saleId.asStateFlow()

    private val _transactionNumber = MutableStateFlow<String?>(null)
    val transactionNumber: StateFlow<String?> = _transactionNumber.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _errorMessage.value = throwable.message ?: "Xatolik yuz berdi"
        _isLoading.value = false
    }

    fun showError(message: String) {
        _errorMessage.value = message
    }

    init {
        generateSaleId()
    }

    fun getOrderData(): OrderDataType? {
        val products = _goods.value
        if (products.isEmpty()) {
            _errorMessage.value = "Mahsulotlar mavjud emas!"
            return null
        }

        val productIds = products.map { it.product.id }
        val quantities = products.map { it.quantity }
        val totalPrice = products.sumOf { it.priceWithDiscount }

        return if (productIds.size == quantities.size) {
            val orderProducts = productIds.mapIndexed { index, productId ->
                OrderProductDataType(productId, quantities[index], 0.0)
            }

            OrderDataType(
                transactionId = _saleId.value ?: "transactionId",
                transactionNumber = _transactionNumber.value?: "transactionNumber",
                products = orderProducts,
                totalPrice = totalPrice,
                description = "",
                customer = null
            )
        } else {
            _errorMessage.value = "Mahsulotlar soni va raqami mos kelmadi!"
            null
        }
    }

    private fun generateSaleId() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            repository.getSaleId()
                .onSuccess {
                    _saleId.value = it.id
                    _transactionNumber.value = it.saleNumber.toString()
                }
                .onFailure {
                    showError("Yangi buyurtma yaratib bo'lmadi 😢")
//                    Need show dialog about retry request to user
                }
        }
    }

    fun incrementQuantity(index: Int) {
        val list = _goods.value.toMutableList()
        val item = list.getOrNull(index) ?: return
        val maxStock = item.product.count
        val unitType = item.product.uniteType

        if (item.quantity < maxStock) {
            list[index] = item.copy(quantity = item.quantity + 1)
            _goods.value = list
        } else {
            showError("Mahsulotlar soni oshib ketdi. Do'konda $maxStock $unitType qolgan!")
        }
    }

    fun decrementQuantity(index: Int) {
        val list = _goods.value.toMutableList()
        val item = list.getOrNull(index) ?: return

        if (item.quantity > 1) {
            list[index] = item.copy(quantity = item.quantity - 1)
        } else {
            list.removeAt(index)
        }

        _goods.value = list
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun getProductById(productId: String?) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            if (productId.isNullOrBlank()) return@launch

            _isLoading.value = true
            val result = repository.getProductById(productId)

            result.onSuccess { product ->
                val updatedList = listOf(
                    CartItem(
                        product,
                        quantity = 1,
                        priceWithDiscount = product.salePrice
                    )
                ) + _goods.value

                _goods.value = updatedList
            }.onFailure {
                showError("Mahsulot topilmadi")
            }

            _isLoading.value = false
        }
    }
}
