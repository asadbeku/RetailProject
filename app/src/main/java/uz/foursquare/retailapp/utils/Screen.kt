package uz.foursquare.retailapp.utils

sealed class Screen(val route: String) {
    object Main : Screen(route = "main_screen")
    object AddProduct : Screen(route = "add_product_screen")
    object Good : Screen(route = "good_screen")
    object Selection : Screen(route = "selection_screen")
    object SelectionWithDescription : Screen(route = "selection_with_description_screen")
}