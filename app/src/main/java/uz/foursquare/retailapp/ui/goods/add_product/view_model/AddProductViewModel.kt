package uz.foursquare.retailapp.ui.goods.add_product.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uz.foursquare.retailapp.ui.goods.add_product.type.request.GoodRequest
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val repository: AddProductRepository
) : ViewModel() {

    private val name: MutableStateFlow<String> = MutableStateFlow("")
    val nameState: StateFlow<String> = name

    private val sku: MutableStateFlow<String> = MutableStateFlow("")
    val skuState: StateFlow<String> = sku

    private val discountPrice: MutableStateFlow<String> = MutableStateFlow("")
    val discountPriceState: StateFlow<String> = discountPrice

    private val unit: MutableStateFlow<String> = MutableStateFlow("")
    val unitState: StateFlow<String> = unit

    private val quantity: MutableStateFlow<String> = MutableStateFlow("")
    val quantityState: StateFlow<String> = quantity

    private val salePrice: MutableStateFlow<String> = MutableStateFlow("")
    val salePriceState: StateFlow<String> = salePrice

    private val barcode: MutableStateFlow<String> = MutableStateFlow("")
    val barcodeState: StateFlow<String> = barcode

    private val purchasePrice: MutableStateFlow<String> = MutableStateFlow("")
    val purchasePriceState: StateFlow<String> = purchasePrice

    private val supplierId: MutableStateFlow<String?> = MutableStateFlow(null)
    val supplierIdState: StateFlow<String?> = supplierId

    private val categoryId: MutableStateFlow<String?> = MutableStateFlow(null)
    val categoryIdState: StateFlow<String?> = categoryId

    private val expirationDate: MutableStateFlow<String> = MutableStateFlow("")
    val expirationDateState: StateFlow<String> = expirationDate

    private val image: MutableStateFlow<String?> = MutableStateFlow(null)
    val imageState: StateFlow<String?> = image

    private val description: MutableStateFlow<String> = MutableStateFlow("")
    val descriptionState: StateFlow<String> = description

    private val status: MutableStateFlow<String> = MutableStateFlow("")
    val statusState: StateFlow<String> = status

    fun setName(name: String) {
        this.name.value = name
    }

    fun setSku(sku: String) {
        this.sku.value = sku
    }

    fun setDiscountPrice(discountPrice: String) {
        this.discountPrice.value = discountPrice
    }

    fun setUnit(unit: String) {
        this.unit.value = unit
    }

    fun setQuantity(quantity: String) {
        this.quantity.value = quantity
    }

    fun setSalePrice(salePrice: String) {
        this.salePrice.value = salePrice
    }

    fun setBarcode(barcode: String) {
        this.barcode.value = barcode
    }

    fun setPurchasePrice(purchasePrice: String) {
        this.purchasePrice.value = purchasePrice
    }

    fun setSupplierId(supplierId: String?) {
        this.supplierId.value = supplierId
    }

    fun setCategoryId(categoryId: String?) {
        this.categoryId.value = categoryId
    }

    fun setExpirationDate(expirationDate: String) {
        this.expirationDate.value = expirationDate
    }

    init {
        addProduct()
    }

    fun addProduct() {

        val product = GoodRequest(
            name = nameState.value,
            sku = skuState.value,
            discount_price = discountPriceState.value,
            unit = unitState.value,
            quantity = quantityState.value,
            sale_price = salePriceState.value,
            barcode = barcodeState.value,
            purchase_price = purchasePriceState.value,
            supplier_id = supplierIdState.value,
            category_id = categoryIdState.value,
            expiration_date = expirationDateState.value
        )

        viewModelScope.launch {
            repository.addProduct(product)
        }
    }

}