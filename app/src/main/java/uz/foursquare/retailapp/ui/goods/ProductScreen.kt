package uz.foursquare.retailapp.ui.goods

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import uz.foursquare.retailapp.ui.theme.RetailAppTheme

@Composable
fun ProductScreen(navController: NavController) {

}


@Preview
@Composable
fun ProductScreenPreview() {
    RetailAppTheme {
        ProductScreen(rememberNavController())
    }
}