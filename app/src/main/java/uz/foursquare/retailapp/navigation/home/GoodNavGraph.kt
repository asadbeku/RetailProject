package uz.foursquare.retailapp.navigation.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import uz.foursquare.retailapp.navigation.Graph
import uz.foursquare.retailapp.ui.goods.add_product.AddProduct
import uz.foursquare.retailapp.ui.goods.selection.SelectionScreen
import uz.foursquare.retailapp.ui.sales.SalesScreen
import uz.foursquare.retailapp.ui.sales.order_success.OrderSuccessScreen
import uz.foursquare.retailapp.ui.sales.payment.PaymentScreen
import uz.foursquare.retailapp.ui.sales.search_product.SearchScreen
import uz.foursquare.retailapp.ui.sales.transaction.ProductTransactionScreen
import uz.foursquare.retailapp.ui.sales.transaction.customer.CustomersScreen
import uz.foursquare.retailapp.utils.SelectionWithDescriptionScreen

fun NavGraphBuilder.addProductNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.ADD_PRODUCT, startDestination = AddProductScreens.AddProduct.route
    ) {
        composable(route = AddProductScreens.AddProduct.route) {
            AddProduct(navController)
        }

        composable(route = AddProductScreens.Good.route) {
            AddProduct(navController)
        }

        composable(
            route = AddProductScreens.Selection.route + "/{screenName}", arguments = listOf(
                navArgument("screenName") { type = NavType.StringType }
            )
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

fun NavGraphBuilder.salesNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.SALES, startDestination = SalesScreen.CartScreen.route
    ) {
        composable(route = SalesScreen.CartScreen.route) {
            SalesScreen(navController)
        }

        composable(route = SalesScreen.SearchScreen.route) {
            SearchScreen(navController)
        }

        composable(route = SalesScreen.PaymentScreen.route) {
            PaymentScreen(navController)
        }

        composable(route = SalesScreen.OrderTransactionScreen.route) {
            ProductTransactionScreen(navController)
        }

        composable(route = SalesScreen.OrderSuccessScreen.route) {
            OrderSuccessScreen(navController)
        }

        composable(route = SalesScreen.CustomerScreen.route) {
            CustomersScreen(navController)
        }
    }
}

sealed class AddProductScreens(val route: String) {
    object AddProduct : AddProductScreens(route = "add_product_screen")
    object Good : AddProductScreens(route = "good_screen")
    object Selection : AddProductScreens(route = "selection_screen")
    object SelectionWithDescription : AddProductScreens(route = "selection_with_description_screen")
}

sealed class SalesScreen(val route: String) {
    object CartScreen : SalesScreen(route = "sales_screen")
    object SearchScreen : SalesScreen(route = "search_screen")
    object PaymentScreen : SalesScreen(route = "payment_screen")
    object OrderTransactionScreen : SalesScreen(route = "order_transaction_screen")
    object OrderSuccessScreen : SalesScreen(route = "order_success_screen")
    object CustomerScreen : SalesScreen(route = "customer_screen")
}