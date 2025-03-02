package uz.foursquare.retailapp.ui.auth.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import uz.foursquare.retailapp.R
import uz.foursquare.retailapp.navigation.auth.AuthScreen
import uz.foursquare.retailapp.ui.theme.AppTheme


@Composable
fun SplashScreen(navController: NavHostController) {
    Scaffold(modifier = Modifier.fillMaxSize(), containerColor = Color.White) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "",
                    tint = AppTheme.color.primary,
                    modifier = Modifier.size(180.dp)
                )
            }
        }
    }

    LaunchedEffect(key1 = true) {
        delay(2000)
        navController.navigate(AuthScreen.Login.route) {
            popUpTo(AuthScreen.Splash.route) {
                inclusive = true
            }
        }
    }
}

@Preview
@Composable
fun IntroScreenPreview() {
    SplashScreen(navController = rememberNavController())
}