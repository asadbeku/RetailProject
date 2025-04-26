package uz.foursquare.retailapp.ui.sales.search_product.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import uz.foursquare.retailapp.ui.goods.goods_screen.types.GoodType
import uz.foursquare.retailapp.utils.logException
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: SearchRepository) : ViewModel() {


    private val _goods = MutableStateFlow<List<GoodType>>(emptyList())
    val goods: StateFlow<List<GoodType>> = _goods.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        val message = when (throwable::class.simpleName) {
            "ConnectException" -> "Internet bilan bog'lanishda xatolik yuz berdi"
            "ConnectTimeoutException" -> "Server bilan ulnishda muammo bor..."
            else -> throwable.message

        }
        _errorMessage.value = message
        _isLoading.value = false
    }

    fun getIdByPosition(position: Int): String {
        return _goods.value[position].id
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun searchByQuery(query: String) {
        if (query.isBlank()) {
            _goods.value = emptyList()
            return
        }

        viewModelScope.launch(exceptionHandler + Dispatchers.IO) {
            _isLoading.value = true

            repository.searchByNames(listOf(query)).onSuccess { products ->
                _goods.value = products.takeIf { it.isNotEmpty() } ?: emptyList()
                _errorMessage.value = if (products.isEmpty()) "No products found" else null
            }.onFailure { error ->
                logException(error, key = "search_product", "Query: $query")
                _errorMessage.value = "Search failed: ${error.message}"
            }

            _isLoading.value = false
        }
    }
}