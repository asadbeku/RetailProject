package uz.foursquare.retailapp.ui.goods.selection.brand.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.foursquare.retailapp.ui.goods.selection.SelectionList
import uz.foursquare.retailapp.ui.goods.selection.brand.types.BrandType
import javax.inject.Inject

@HiltViewModel
class BrandViewModel @Inject constructor(private val repository: BrandRepository) : ViewModel() {

    private val _selectionList = MutableStateFlow<SelectionList>(SelectionList.Unknown)
    val selectionList: StateFlow<SelectionList> = _selectionList.asStateFlow()


    private val _brandList = MutableStateFlow<List<BrandType>>(emptyList())
    val brandList: StateFlow<List<BrandType>> = _brandList

    private val _errorMessage = MutableStateFlow<String>("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _brandName = MutableStateFlow("")
    val brandName: StateFlow<String> = _brandName

    fun updateCategoryName(newName: String) {
        _brandName.value = newName
    }

    init {
        getCategoryList()
    }

    fun getCategoryList() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getBrands()
            if (result.isSuccess) {
                _brandList.value = result.getOrThrow()
                _isLoading.value = false
            } else {
                _brandList.value = emptyList()
                _errorMessage.value =
                    "Failed to load categories: ${result.exceptionOrNull()?.message}"
                _isLoading.value = false

            }
        }
    }

    fun addCategory() {
        viewModelScope.launch {
            val result = repository.addBrand(_brandName.value)
            if (result.isSuccess) {
                _brandName.value = ""
                getCategoryList()
                _isLoading.value = true
            } else {
                _errorMessage.value = "Failed to add category: ${result.exceptionOrNull()?.message}"
            }
        }
    }
}