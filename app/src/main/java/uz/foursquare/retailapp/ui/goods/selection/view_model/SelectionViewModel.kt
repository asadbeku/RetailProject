package uz.foursquare.retailapp.ui.goods.selection.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uz.foursquare.retailapp.ui.goods.selection.SelectedItem
import uz.foursquare.retailapp.ui.goods.selection.brand.types.BrandType
import uz.foursquare.retailapp.ui.goods.selection.brand.view_model.BrandRepository
import uz.foursquare.retailapp.ui.goods.selection.category.types.CategoryType
import uz.foursquare.retailapp.ui.goods.selection.category.view_model.CategoryRepository
import uz.foursquare.retailapp.ui.goods.selection.product_unit.ProductUnitRepository
import uz.foursquare.retailapp.ui.goods.selection.product_unit.type.ProductUnitType
import uz.foursquare.retailapp.ui.goods.selection.suppliers.types.SupplierType
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

    private val _brandList = MutableStateFlow<List<BrandType>>(emptyList())
    private val _categoryList = MutableStateFlow<List<CategoryType>>(emptyList())
    private val _supplierList = MutableStateFlow<List<SupplierType>>(emptyList())
    private val _productUnitList = MutableStateFlow<List<ProductUnitType>>(emptyList())

    fun updateSelectedName(newName: String) {
        _selectedName.value = newName
    }

    fun loadSelectionList(type: SelectionType) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = when (type) {
                SelectionType.BRAND -> brandRepository.getBrands()
                    .mapCatching {
                        _brandList.value = it
                        it.map { brand -> brand.name }
                    }

                SelectionType.CATEGORY -> categoryRepository.getCategories()
                    .mapCatching {
                        _categoryList.value = it
                        it.map { category -> category.name }
                    }

                SelectionType.SUPPLIER -> suppliersRepository.getSuppliers()
                    .mapCatching {
                        _supplierList.value = it
                        it.map { supplier -> supplier.name }
                    }

                SelectionType.PRODUCT_UNIT -> productUnitRepository.getProductUnits()
                    .mapCatching {
                        _productUnitList.value = it
                        it.map { unit -> unit.name }
                    }
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

    fun selectedItem(category: SelectionType?, index: Int): SelectedItem {
        val id = when (category) {
            SelectionType.BRAND -> SelectedItem(
                _brandList.value[index].id,
                _brandList.value[index].name,
                "brand"
            )

            SelectionType.CATEGORY -> SelectedItem(
                _categoryList.value[index].id,
                _categoryList.value[index].name,
                "category"
            )

            SelectionType.SUPPLIER -> SelectedItem(
                _supplierList.value[index].id,
                _supplierList.value[index].name,
                "suppliers"
            )

            SelectionType.PRODUCT_UNIT -> SelectedItem(
                _productUnitList.value[index].name,
                _productUnitList.value[index].name,
                "product_unit"
            )

            else -> {
                _errorMessage.value = "Bunday kategoriya mavjud emas!"
                SelectedItem("", "", "")

            }
        }

        Log.d("TAG", "selectedItem: $id")

        return id
    }
}
