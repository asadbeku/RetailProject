package uz.foursquare.retailapp.navigation.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import uz.foursquare.retailapp.navigation.Graph
import uz.foursquare.retailapp.ui.goods.ProductScreen
import uz.foursquare.retailapp.ui.goods.add_product.AddProduct
import uz.foursquare.retailapp.utils.SelectionScreen
import uz.foursquare.retailapp.utils.SelectionWithDescriptionScreen

fun NavGraphBuilder.addProductNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.ADD_PRODUCT, startDestination = AddProductScreens.AddProduct.route
    ) {
        composable(route = AddProductScreens.AddProduct.route) {
            AddProduct(navController)
        }

        composable(route = AddProductScreens.Good.route) {
            ProductScreen(navController)
        }

        composable(
            route = AddProductScreens.Selection.route + "/{screenName}", arguments = listOf(
                navArgument("screenName") { type = NavType.StringType })
        ) { backStackEntry ->
            SelectionScreen(
                navController = navController,
                screenName = backStackEntry.arguments?.getString("screenName") ?: "Ekran nomi"
            )
        }

        composable(route = AddProductScreens.SelectionWithDescription.route) {
            SelectionWithDescriptionScreen(navController = navController)
        }
    }
}

sealed class AddProductScreens(val route: String) {
    object AddProduct : AddProductScreens(route = "add_product_screen")
    object Good : AddProductScreens(route = "good_screen")
    object Selection : AddProductScreens(route = "selection_screen")
    object SelectionWithDescription : AddProductScreens(route = "selection_with_description_screen")
}