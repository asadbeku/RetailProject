package uz.foursquare.retailapp.ui.sales.transaction.view_model

import javax.inject.Inject


class ProductTransactionRepository @Inject constructor() {

    fun getSeller(): Result<String> {
        return Result.success("Salimjonov Sardorbek")
    }


}