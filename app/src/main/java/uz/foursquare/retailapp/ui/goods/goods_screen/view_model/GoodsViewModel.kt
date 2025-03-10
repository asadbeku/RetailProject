package uz.foursquare.retailapp.ui.goods.goods_screen.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uz.foursquare.retailapp.ui.goods.goods_screen.types.GoodType
import javax.inject.Inject

@HiltViewModel
class GoodsViewModel @Inject constructor(private val repository: GoodsRepository) : ViewModel() {

    init {
        getGoods()
    }

    private val _goods = MutableStateFlow<List<GoodType>>(emptyList())
    val goods: StateFlow<List<GoodType>> = _goods

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String>("")
    val errorMessage: StateFlow<String> = _errorMessage

    private fun getGoods() {
        viewModelScope.launch {
            val result = repository.getGoods()
            if (result.isSuccess) {
                val listProducts = result.getOrNull()
                if (listProducts != null) {
                    _goods.emit(listProducts)
                } else {
                    _errorMessage.emit("List of products is null")
                }
            }
        }
    }

}