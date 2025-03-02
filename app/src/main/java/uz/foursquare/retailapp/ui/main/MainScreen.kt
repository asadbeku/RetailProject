package uz.foursquare.retailapp.ui.main

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import uz.foursquare.retailapp.R
import uz.foursquare.retailapp.navigation.home.BottomNavigationGraph
import uz.foursquare.retailapp.ui.theme.AppTheme
import uz.foursquare.retailapp.ui.theme.RetailAppTheme

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    RetailAppTheme {
        Scaffold(
            bottomBar = { BottomBar(navController = navController) }
        ) { innerPadding ->
            BottomNavigationGraph(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )

        }
    }

}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Goods,
        BottomBarScreen.Sales,
        BottomBarScreen.Transactions,
        BottomBarScreen.Menu
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination

    val bottomBarDestination = screens.any { it.route == currentRoute?.route }
    if (bottomBarDestination) {
        NavigationBar(containerColor = Color.White, contentColor = AppTheme.color.primary) {
            screens.forEach { screen ->
                AddItem(
                    screen = screen,
                    currentRoute = currentRoute,
                    navController = navController
                )
            }
        }
    }

}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentRoute: NavDestination?,
    navController: NavController
) {
    NavigationBarItem(
        selected = currentRoute?.hierarchy?.any() {
            it.route == screen.route
        } == true,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        },
        icon = {
            if (screen.icon != null) {
                Icon(
                    imageVector = screen.icon,
                    contentDescription = "Navigation Icon"
                )
            } else {
                Icon(
                    painter = painterResource(screen.painterIcon ?: 0),
                    contentDescription = "Navigation Icon"
                )
            }

        },
        label = {
            Text(text = screen.title)
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = AppTheme.color.primary,
            selectedTextColor = AppTheme.color.primary,
            indicatorColor = AppTheme.appColor.highlightLightest,
            unselectedIconColor = AppTheme.appColor.neutralDarkLight,
            unselectedTextColor = AppTheme.appColor.neutralDarkLight
        )
    )

}


@Preview(showBackground = true)
@Composable
fun PreviewScreen() {
    RetailAppTheme {
        MainScreen()
    }
}