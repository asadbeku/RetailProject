package uz.foursquare.retailapp.ui.goods.selection.suppliers.types

data class SupplierType(
    val id: String,
    val name: String,
    val createdAt: String,
    val updatedAt: String,
    val phoneNumber: String?,
    val balance: Double?,
    val debitValue: Double?,
    val totalPaidPurchasePrice: Double?,
    val totalPurchasePrice: Double?,
    val totalPurchaseMeasurementValue: Double?
)
