package uz.foursquare.retailapp.ui.goods.selection.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uz.foursquare.retailapp.ui.goods.selection.brand.view_model.BrandRepository
import uz.foursquare.retailapp.ui.goods.selection.category.view_model.CategoryRepository
import uz.foursquare.retailapp.ui.goods.selection.product_unit.ProductUnitRepository
import uz.foursquare.retailapp.ui.goods.selection.suppliers.view_model.SuppliersRepository
import uz.foursquare.retailapp.utils.SelectionType
import javax.inject.Inject

@HiltViewModel
class SelectionViewModel @Inject constructor(
    private val brandRepository: BrandRepository,
    private val categoryRepository: CategoryRepository,
    private val suppliersRepository: SuppliersRepository,
    private val productUnitRepository: ProductUnitRepository
) : ViewModel() {

    private val _selectionList = MutableStateFlow<List<String>>(emptyList())
    val selectionList: StateFlow<List<String>> = _selectionList

    private val _errorMessage = MutableStateFlow<String>("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _selectedName = MutableStateFlow("")
    val selectedName: StateFlow<String> = _selectedName


    fun updateSelectedName(newName: String) {
        _selectedName.value = newName
    }

    fun loadSelectionList(type: SelectionType) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = when (type) {
                SelectionType.BRAND -> brandRepository.getBrands()
                    .mapCatching { it.map { brand -> brand.name } }

                SelectionType.CATEGORY -> categoryRepository.getCategories()
                    .mapCatching { it.map { category -> category.name } }

                SelectionType.SUPPLIER -> suppliersRepository.getSuppliers()
                    .mapCatching { it.map { supplier -> supplier.name } }

                SelectionType.PRODUCT_UNIT -> productUnitRepository.getProductUnits()
                    .mapCatching { it.map { unit -> unit.name } }
            }

            result.fold(
                onSuccess = { _selectionList.value = it },
                onFailure = { _errorMessage.value = "Failed to load data: ${it.message}" }
            )

            _isLoading.value = false
        }
    }

    fun addSelection(type: SelectionType) {
        viewModelScope.launch {
            if (_selectedName.value.isBlank()) {
                _errorMessage.value = "Name cannot be empty!"
                return@launch
            }

            val result = when (type) {
                SelectionType.BRAND -> brandRepository.addBrand(_selectedName.value)
                SelectionType.CATEGORY -> categoryRepository.addCategory(_selectedName.value)
                SelectionType.SUPPLIER -> suppliersRepository.addSupplier(_selectedName.value)
                SelectionType.PRODUCT_UNIT -> productUnitRepository.addProductUnit(_selectedName.value)
            }

            result.fold(
                onSuccess = {
                    _selectedName.value = ""
                    loadSelectionList(type) // Reload data after addition
                },
                onFailure = { _errorMessage.value = "Failed to add: ${it.message}" }
            )
        }
    }
}
