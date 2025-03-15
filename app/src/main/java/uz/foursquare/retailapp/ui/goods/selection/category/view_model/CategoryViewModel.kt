package uz.foursquare.retailapp.ui.goods.selection.category.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uz.foursquare.retailapp.ui.goods.selection.category.types.CategoryType
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: CategoryRepository
) : ViewModel() {

    private val _categoryList = MutableStateFlow<List<CategoryType>>(emptyList())
    val categoryList: StateFlow<List<CategoryType>> = _categoryList

    private val _errorMessage = MutableStateFlow<String>("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _categoryName = MutableStateFlow("")
    val categoryName: StateFlow<String> = _categoryName

    fun updateCategoryName(newName: String) {
        _categoryName.value = newName
    }

    init {
        getCategoryList()
    }

    fun getCategoryList() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getCategories()
            if (result.isSuccess) {
                _categoryList.value = result.getOrThrow()
                _isLoading.value = false
            } else {
                _categoryList.value = emptyList()
                _errorMessage.value =
                    "Failed to load categories: ${result.exceptionOrNull()?.message}"
                _isLoading.value = false

            }
        }
    }

    fun addCategory() {
        viewModelScope.launch {
            val result = repository.addCategory(_categoryName.value)
            if (result.isSuccess) {
                _categoryName.value = ""
                getCategoryList()
                _isLoading.value = true
            } else {
                _errorMessage.value = "Failed to add category: ${result.exceptionOrNull()?.message}"
            }
        }
    }

}