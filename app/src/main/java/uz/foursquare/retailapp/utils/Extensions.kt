package uz.foursquare.retailapp.utils

import android.icu.text.NumberFormat
import com.google.firebase.crashlytics.FirebaseCrashlytics
import uz.foursquare.retailapp.ui.goods.goods_screen.types.GoodType
import uz.foursquare.retailapp.ui.goods.goods_screen.types.response.GoodsTypeResponse
import uz.foursquare.retailapp.ui.goods.goods_screen.types.response.ProductData
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

fun String.getProductUnitServer(): String {
    return when (this) {
        "dona" -> "pcs"
        "metr" -> "m"
        "metr²" -> "m2"
        "metr³" -> "m3"
        else -> this
    }


}

fun ProductData.convertToGoodType(): GoodType {
    return this@convertToGoodType.let {
        GoodType(
            id = id,
            name = name,
            count = quantity,
            salePrice = salePrice.toDouble(),
            purchasePrice = purchasePrice.toDouble(),
            purchasePriceUSD = purchasePriceUsd.toDouble(),
            salePriceUSD = salePriceUsd.toDouble(),
            uniteType = unit,
            barcode = barcode,
        )
    }
}

fun logException(exceptionName: Throwable, key: String, customData: String) {
    val crashlytics = FirebaseCrashlytics.getInstance()

    // Add custom key-value data
    crashlytics.setCustomKey(key, customData)

    // Record the exception properly
    crashlytics.recordException(exceptionName)
}
