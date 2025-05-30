package uz.foursquare.retailapp.ui.goods.add_product.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.foursquare.retailapp.ui.goods.add_product.type.request.GoodRequest
import uz.foursquare.retailapp.utils.getProductUnitServer
import uz.foursquare.retailapp.utils.logException
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val repository: AddProductRepository
) : ViewModel() {

    private var _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()

    private var _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var _isAdded = MutableStateFlow(false)
    val isAdded: StateFlow<Boolean> = _isAdded.asStateFlow()


    private val _name = MutableStateFlow("")
    val nameState: StateFlow<String> = _name

    private val _sku = MutableStateFlow("")
    val skuState: StateFlow<String> = _sku

    private val _discountPrice = MutableStateFlow("")
    val discountPriceState: StateFlow<String> = _discountPrice

    private val _unit = MutableStateFlow("")
    val unitState: StateFlow<String> = _unit

    private val _quantity = MutableStateFlow("")
    val quantityState: StateFlow<String> = _quantity

    private val _salePrice = MutableStateFlow("")
    val salePriceState: StateFlow<String> = _salePrice

    private val _barcode = MutableStateFlow("")
    val barcodeState: StateFlow<String> = _barcode

    private val _purchasePrice = MutableStateFlow("")
    val purchasePriceState: StateFlow<String> = _purchasePrice

    private val _supplier = MutableStateFlow<Map<String, String>>(emptyMap())
    val supplierState: StateFlow<Map<String, String>> = _supplier

    private val _category = MutableStateFlow<Map<String, String>>(emptyMap())
    val categoryState: StateFlow<Map<String, String>> = _category

    private val _brand = MutableStateFlow<Map<String, String>>(emptyMap())
    val brandState: StateFlow<Map<String, String>> = _brand

    private val _expirationDate = MutableStateFlow("")
    val expirationDateState: StateFlow<String> = _expirationDate

    private val _image = MutableStateFlow<String?>(null)
    val imageState: StateFlow<String?> = _image

    private val _description = MutableStateFlow("")
    val descriptionState: StateFlow<String> = _description

    private val _status = MutableStateFlow("")
    val statusState: StateFlow<String> = _status

    fun setName(value: String) {
        _name.value = value
    }

    fun setSku(value: String) {
        _sku.value = value
    }

    fun setDiscountPrice(value: String) {
        _discountPrice.value = value
    }

    fun setUnit(value: String) {
        _unit.value = value
    }

    fun setQuantity(value: String) {
        _quantity.value = value
    }

    fun setSalePrice(value: String) {
        _salePrice.value = value
    }

    fun setBarcode(value: String) {
        _barcode.value = value
    }

    fun setPurchasePrice(value: String) {
        _purchasePrice.value = value
    }

    fun setBrand(brand: Map<String, String>) {
        _brand.value = brand
    }

    fun setSupplier(supplier: Map<String, String>) {
        _supplier.value = supplier
    }

    fun setCategory(category: Map<String, String>) {
        _category.value = category
    }

    fun setExpirationDate(value: String) {
        _expirationDate.value = value
    }

    fun clearError() {
        _errorMessage.value = ""
    }

    fun addProduct() {
        val product = GoodRequest(
            name = nameState.value,
            sku = skuState.value,
            discount_price = discountPriceState.value,
            unit = unitState.value.toString().getProductUnitServer(),
            quantity = quantityState.value.toIntOrNull() ?: 0,
            sale_price = salePriceState.value,
            barcode = barcodeState.value,
            purchase_price = purchasePriceState.value,
            supplier_id = supplierState.value.keys.firstOrNull(),
            category_id = categoryState.value.keys.firstOrNull(),
            brand_id = brandState.value.keys.firstOrNull(),
            expiration_date = expirationDateState.value
        )

        viewModelScope.launch {
            _isLoading.value = true

            val result = repository.addProduct(product)

            result.onSuccess {
                _isLoading.value = false
                _isAdded.value = true
            }.onFailure {
                logException(it, "add_product", product.toString())
                _isLoading.value = false
                _errorMessage.value = it.message ?: "Tovar qo'shib bo'lmadi :("
            }
        }
    }
}