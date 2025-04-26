package uz.foursquare.retailapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import uz.foursquare.retailapp.navigation.auth.authNavGraph
import uz.foursquare.retailapp.ui.main.MainScreen

@Composable
fun RootNavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.AUTHENTICATION,
        builder = {
            authNavGraph(navController = navController)
            composable(route = Graph.MAIN) {
                MainScreen()
            }
        }
    )
}

object Graph {
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "auth_graph"
    const val MAIN = "main_graph"
    const val ADD_PRODUCT = "add_product_graph"
    const val SALES = "sales_graph"
}