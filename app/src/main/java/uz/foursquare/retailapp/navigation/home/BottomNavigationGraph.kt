package uz.foursquare.retailapp.navigation.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import uz.foursquare.retailapp.ui.goods.goods_screen.GoodsScreen
import uz.foursquare.retailapp.ui.home.HomeScreen
import uz.foursquare.retailapp.ui.main.BottomBarScreen
import uz.foursquare.retailapp.ui.menu.MenuScreen
import uz.foursquare.retailapp.ui.sales.SalesScreen
import uz.foursquare.retailapp.ui.transaction.TransactionsScreen

@Composable
fun BottomNavigationGraph(navController: NavHostController, modifier: Modifier) {
    NavHost(
        navController, startDestination = BottomBarScreen.Home.route, modifier = modifier
    ) {
        composable(BottomBarScreen.Home.route) {
            HomeScreen(navController)
        }
        composable(BottomBarScreen.Goods.route) {
            GoodsScreen(navController)
        }
        composable(BottomBarScreen.Sales.route) {
            SalesScreen(navController)
        }

        composable(BottomBarScreen.Transactions.route) {
            TransactionsScreen(navController)
        }
        composable(BottomBarScreen.Menu.route) {
            MenuScreen(navController)
        }

        addProductNavGraph(navController)

        salesNavGraph(navController)
    }

}