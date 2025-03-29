package uz.foursquare.retailapp.ui.goods.selection

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SelectedItem(
    val id: String,
    val name: String,
    val screenName: String
) : Parcelable
