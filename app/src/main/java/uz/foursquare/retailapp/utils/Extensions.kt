package uz.foursquare.retailapp.utils

import android.icu.text.NumberFormat
import uz.foursquare.retailapp.ui.goods.goods_screen.types.GoodType
import uz.foursquare.retailapp.ui.goods.goods_screen.types.response.GoodsTypeResponse
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Double.convertToPriceFormat(): String {
    return try {
        val formatted = NumberFormat.getNumberInstance(Locale.US)
            .apply { isGroupingUsed = true }
            .format(this)
            .replace(",", " ")
        "$formatted UZS"
    } catch (e: Exception) {
        "0 UZS"
    }
}

fun Long.millisToDate(): String {
    val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return sdf.format(Date(this))
}

fun Long.millisToTime(): String {
    val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return sdf.format(Date(this))
}

fun GoodsTypeResponse.toGoodTypeList(): List<GoodType> {
    return data.map {
        GoodType(
            id = it.id,
            name = it.name,
            salePrice = it.salePrice.toDouble(),
            purchasePrice = it.purchasePrice.toDouble(),
            purchasePriceUSD = it.purchasePriceUsd.toDouble(),
            salePriceUSD = it.salePriceUsd.toDouble(),
            count = it.quantity,
            uniteType = it.unit,
            barcode = it.barcode
        )
    }
}