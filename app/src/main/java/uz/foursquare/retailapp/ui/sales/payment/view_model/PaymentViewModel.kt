package uz.foursquare.retailapp.ui.sales.payment.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.foursquare.retailapp.ui.sales.payment.types.PaymentAmounts
import uz.foursquare.retailapp.ui.sales.payment.types.PaymentMethod
import uz.foursquare.retailapp.ui.sales.payment.types.PaymentOption
import uz.foursquare.retailapp.ui.sales.type.OrderDataType
import uz.foursquare.retailapp.ui.sales.type.sale_request.Order
import uz.foursquare.retailapp.ui.sales.type.sale_request.Payment
import uz.foursquare.retailapp.ui.sales.type.sale_request.SaleRequestType
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val repository: PaymentRepository
) : ViewModel() {

    private val _orderTotal = MutableStateFlow(0.0)
    val orderTotal = _orderTotal.asStateFlow()

    private var _orderData = MutableStateFlow<OrderDataType?>(null)
    val orderData: StateFlow<OrderDataType?> = _orderData.asStateFlow()

    private val _paymentAmounts = MutableStateFlow(PaymentAmounts())
    val paymentAmounts = _paymentAmounts.asStateFlow()

    private val _paymentOptions = MutableStateFlow(
        listOf(
            PaymentOption(PaymentMethod.CASH, "Naqd", true),
            PaymentOption(PaymentMethod.CARD, "Karta", false),
            PaymentOption(PaymentMethod.CREDIT, "Qarzga", false)
        )
    )
    val paymentOptions = _paymentOptions.asStateFlow()

    val remainingAmount = combine(orderTotal, paymentAmounts) { total, amounts ->
        (total - amounts.totalPaid)
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _errorMessage.value = throwable.message ?: "Xatolik yuz berdi"
        _isLoading.value = false
    }

    fun showError(message: String) {
        _errorMessage.value = message
    }

    fun setOrderTotal(price: Double) {
        Log.d("TAG", "order price: $price")
        _orderTotal.value = price
    }

    fun setOrderData(dataType: OrderDataType) {
        Log.d("CheckLog", "order data: $dataType")
        _orderData.value = dataType
    }

    fun selectPaymentOption(method: PaymentMethod) {
        _paymentOptions.update { options ->
            options.map { option ->
                if (option.method == method) {
                    if (option.isSelected == true) {
                        updatePaymentAmount(method, 0.0f)
                    }
                    option.copy(isSelected = !option.isSelected)

                } else option
            }
        }
    }

    fun updatePaymentAmount(method: PaymentMethod, amount: Float) {
        _paymentAmounts.update { current ->
            when (method) {
                PaymentMethod.CASH -> current.copy(cash = amount)
                PaymentMethod.CARD -> current.copy(card = amount)
                PaymentMethod.CREDIT -> current.copy(credit = amount)
            }
        }
    }

    private var _selectedPaymentTypeIndexes = MutableStateFlow<List<Int>>(listOf(0))
    val selectedPaymentTypeIndexes = _selectedPaymentTypeIndexes.asStateFlow()

    fun reset() {
        _orderTotal.value = 0.0
        _paymentAmounts.value = PaymentAmounts()
        _paymentOptions.value = _paymentOptions.value.map {
            it.copy(isSelected = it.method == PaymentMethod.CASH)
        }
    }

    fun completeOrder() {
        val products = orderData.value?.products?.map { product ->
            Order(
                discount = product.discount,
                productId = product.productId,
                quantity = product.quantity
            )

        }

        val payments = paymentOptions.value.map { payment ->
            when (payment.method) {
                PaymentMethod.CASH -> Payment(
                    amount = paymentAmounts.value.cash.toDouble(),
                    method = "cash",
                )

                PaymentMethod.CARD -> Payment(
                    amount = paymentAmounts.value.credit.toDouble(),
                    method = "credit"
                )

                PaymentMethod.CREDIT -> Payment(
                    amount = paymentAmounts.value.credit.toDouble(),
                    method = "credit"
                )
            }
        }

        val order = SaleRequestType(
            saleId = orderData.value?.transactionId.toString(),
            clientId = orderData.value?.customer?.id.toString(),
            orders = products ?: emptyList(),
            payments = payments,
        )

        viewModelScope.launch(exceptionHandler + Dispatchers.IO) {
            repository.saveOrder(order)
                .onSuccess {
                    Log.d("TAG", it.toString())
                }
                .onFailure {
                    _errorMessage.value = it.message
                }
        }
    }
}