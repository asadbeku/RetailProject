package uz.foursquare.retailapp.ui.goods.add_product.type.request

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable

@Serializable
data class GoodRequest(
    val name: String,
    val sku: String,
    val discount_price: String,
    val unit: String,
    val quantity: Int,
    val sale_price: String,
    val barcode: String,
    val purchase_price: String,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val supplier_id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val category_id: String? = null,
    val expiration_date: String
)
