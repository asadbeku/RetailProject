package uz.foursquare.retailapp.navigation.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import uz.foursquare.retailapp.navigation.Graph
import uz.foursquare.retailapp.ui.auth.login.LoginScreen
import uz.foursquare.retailapp.ui.auth.select_company.SelectCompanyScreen
import uz.foursquare.retailapp.ui.auth.splash.SplashScreen

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.AUTHENTICATION, startDestination = AuthScreen.Splash.route
    ) {
        composable(route = AuthScreen.Splash.route) { SplashScreen(navController = navController) }
        composable(route = AuthScreen.Login.route) { LoginScreen(navController = navController) }
        composable(route = AuthScreen.SelectCompany.route) { SelectCompanyScreen(navController = navController) }
    }
}

sealed class AuthScreen(val route: String) {
    object Splash : AuthScreen(route = "splash_screen")
    object Login : AuthScreen(route = "login_screen")
    object SelectCompany : AuthScreen(route = "select_company_screen")
}