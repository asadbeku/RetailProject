package uz.foursquare.retailapp.ui.goods.selection.suppliers.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uz.foursquare.retailapp.ui.goods.selection.category.types.CategoryType
import uz.foursquare.retailapp.ui.goods.selection.suppliers.types.SupplierType
import javax.inject.Inject

@HiltViewModel
class SuppliersViewModel @Inject constructor(
    private val repository: SuppliersRepository
) : ViewModel() {
    private val _supplierList = MutableStateFlow<List<SupplierType>>(emptyList())
    val supplierList: StateFlow<List<SupplierType>> = _supplierList

    private val _errorMessage = MutableStateFlow<String>("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _supplierName = MutableStateFlow("")
    val supplierName: StateFlow<String> = _supplierName

    fun updateSupplierName(newName: String) {
        _supplierName.value = newName
    }

    init {
        getSupplierList()
    }

    fun getSupplierList() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getSuppliers()
            if (result.isSuccess) {
                _supplierList.value = result.getOrThrow()
                _isLoading.value = false
            } else {
                _supplierList.value = emptyList()
                _errorMessage.value =
                    "Failed to load categories: ${result.exceptionOrNull()?.message}"
                _isLoading.value = false
            }
        }
    }

    fun addSupplier() {
        viewModelScope.launch {
            val result = repository.addSupplier(_supplierName.value)
            if (result.isSuccess) {
                _supplierName.value = ""
                getSupplierList()
                _isLoading.value = true
            } else {
                _errorMessage.value = "Failed to add category: ${result.exceptionOrNull()?.message}"
            }
        }
    }
}