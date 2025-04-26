package uz.foursquare.retailapp.ui.sales.order_success

import uz.foursquare.retailapp.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uz.foursquare.retailapp.ui.theme.AppTheme
import uz.foursquare.retailapp.ui.theme.RetailAppTheme

@Composable
fun OrderSuccessScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    RetailAppTheme {
        Scaffold(
            containerColor = AppTheme.appColor.supportSuccessMedium
        ) { innerPadding ->
            Column(modifier = modifier.padding(innerPadding)) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(R.drawable.check_circle_icon),
                            contentDescription = null,
                            modifier = Modifier.size(54.dp),
                            tint = Color.White
                        )

                        Text(
                            text = "Success",
                            color = Color.White,
                            style = AppTheme.typography.headlineH1
                        )

                        Row(modifier = Modifier.padding(top = 54.dp)) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    Icons.Default.ArrowBackIosNew,
                                    contentDescription = null,
                                    modifier = Modifier.size(36.dp),
                                    tint = Color.White
                                )

                                Text(
                                    text = "Ortga",
                                    color = Color.White,
                                    style = AppTheme.typography.headlineH3
                                )

                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    Icons.Default.Print,
                                    contentDescription = null,
                                    modifier = Modifier.size(36.dp),
                                    tint = Color.White
                                )

                                Text(
                                    text = "Chop etish",
                                    color = Color.White,
                                    style = AppTheme.typography.headlineH3
                                )

                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    Icons.Default.Share,
                                    contentDescription = null,
                                    modifier = Modifier.size(36.dp),
                                    tint = Color.White
                                )

                                Text(
                                    text = "Ulashish",
                                    color = Color.White,
                                    style = AppTheme.typography.headlineH3
                                )

                            }
                        }
                    }


                }
            }
        }
    }
}


@Preview
@Composable
fun OrderSuccessScreenPreview(modifier: Modifier = Modifier) {
    OrderSuccessScreen(rememberNavController(), modifier)
}