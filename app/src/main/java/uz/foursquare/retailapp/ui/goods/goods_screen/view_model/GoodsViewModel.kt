package uz.foursquare.retailapp.ui.goods.goods_screen.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.foursquare.retailapp.ui.goods.goods_screen.types.GoodType
import javax.inject.Inject

@HiltViewModel
class GoodsViewModel @Inject constructor(private val repository: GoodsRepository) : ViewModel() {

    private val _goods = MutableStateFlow<List<GoodType>>(emptyList())
    val goods: StateFlow<List<GoodType>> = _goods.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()

    init {
        getGoods()
    }

    fun clearError() {
        _errorMessage.value = ""
    }

    private fun getGoods() {
        viewModelScope.launch {
            _isLoading.value = true  // Update state synchronously
            try {
                val result = repository.getGoods()
                delay(500)  // Simulate a loading delay (optional)
                _isLoading.value = false

                result.getOrNull()?.let { products ->
                    _goods.value = products
                } ?: run {
                    _errorMessage.value = "List of products is null"
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "Failed to load goods: ${e.message}"
            }
        }
    }
}