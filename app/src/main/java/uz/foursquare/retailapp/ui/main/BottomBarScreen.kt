package uz.foursquare.retailapp.ui.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import uz.foursquare.retailapp.R

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector? = null,
    val painterIcon: Int? = null
) {
    object Home : BottomBarScreen(
        "home",
        "Asosiy",
        icon = Icons.Default.Home
    )

    object Goods : BottomBarScreen(
        "goods",
        "Tovarlar",
        icon = Icons.Default.ShoppingCart
    )

    object Sales : BottomBarScreen(
        "sales",
        "Sotuvlar",
        painterIcon = R.drawable.point_of_sale
    )

    object Transactions : BottomBarScreen(
        "transactions",
        "Hisobotlar",
        painterIcon = R.drawable.report_icon
    )

    object Menu : BottomBarScreen(
        "menu",
        "Menu",
        icon = Icons.Default.Menu
    )


}