package uz.foursquare.retailapp.ui.goods.add_product.view_model

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.foursquare.retailapp.ui.goods.add_product.type.request.GoodRequest
import uz.foursquare.retailapp.ui.goods.goods_screen.types.GoodType
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val repository: AddProductRepository
) : ViewModel() {

    fun addProduct(product: GoodRequest) {
        repository.addProduct(product)
    }

}