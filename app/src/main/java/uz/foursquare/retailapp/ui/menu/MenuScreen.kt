package uz.foursquare.retailapp.ui.menu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import uz.foursquare.retailapp.R
import uz.foursquare.retailapp.ui.theme.AppTheme
import uz.foursquare.retailapp.ui.theme.RetailAppTheme

@Composable
fun MenuScreen(navController: NavController) {
    RetailAppTheme {
        Scaffold(
            topBar = { MenuToolbar(title = "Menu") },
            containerColor = AppTheme.appColor.neutralLightLight
        ) { innerPadding ->
            LazyColumn(modifier = Modifier.padding(innerPadding)) {

                item { GoodsCard() }

                item { SalesCard() }

                item { ClientsCard() }

                item { ReportsCard() }

            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuToolbar(
    title: String, onNavigationIconClick: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                title,
                color = MaterialTheme.colorScheme.onSurface,
                style = AppTheme.typography.headlineH3
            )
        },
        actions = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = "Back",
                    modifier = Modifier.size(30.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            titleContentColor = Color.Black
        ),
        modifier = Modifier.clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
    )
}

@Composable
fun GoodsCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    tint = AppTheme.color.primary,
                    modifier = Modifier.padding(end = 8.dp)
                )

                Text(
                    text = "Maxsulotlar",
                    fontSize = 20.sp,
                    style = AppTheme.typography.headlineH3,
                    modifier = Modifier
                )
            }

            HorizontalDivider()


            Row(
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Button(
                    onClick = {
                        throw Exception("Test exception")
                    }, modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppTheme.color.primary)
                ) {
                    Text(text = "Katalog")
                }
                Button(
                    onClick = {}, modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppTheme.color.primary)
                ) {
                    Text(text = "Buyurtmalar")
                }

            }

            Row {
                Button(
                    onClick = {}, modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppTheme.color.primary)
                ) {
                    Text(text = "Inventarizatsiya")
                }
                Button(
                    onClick = {}, modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppTheme.color.primary)
                ) {
                    Text(text = "Transfer")
                }

            }

            Row {
                Button(
                    onClick = {}, modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppTheme.color.primary)
                ) {
                    Text(text = "Qayta baholash")
                }
                Button(
                    onClick = {}, modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppTheme.color.primary)
                ) {
                    Text(text = "Hisobdan chiqarish")
                }

            }
            Row {
                Button(
                    onClick = {}, modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppTheme.color.primary)
                ) {
                    Text(text = "Yetkazib beruvchi")
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp)
                )

            }
        }

    }
}

@Composable
fun SalesCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.barcode_scanner),
                    contentDescription = null,
                    tint = AppTheme.color.primary,
                    modifier = Modifier.padding(end = 8.dp)
                )

                Text(
                    text = "Sotuvlar",
                    fontSize = 20.sp,
                    style = AppTheme.typography.headlineH3,
                    modifier = Modifier
                )
            }

            HorizontalDivider()


            Row(
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Button(
                    onClick = {}, modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppTheme.color.primary)
                ) {
                    Text(text = "Yangi sotuv")
                }
                Button(
                    onClick = {}, modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppTheme.color.primary)
                ) {
                    Text(text = "Tranzaksiyalar")
                }
            }
        }
    }
}

@Composable
fun ClientsCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = AppTheme.color.primary,
                    modifier = Modifier.padding(end = 8.dp)
                )

                Text(
                    text = "Mijozlar",
                    fontSize = 20.sp,
                    style = AppTheme.typography.headlineH3,
                    modifier = Modifier
                )
            }

            HorizontalDivider()


            Row(
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Button(
                    onClick = {}, modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppTheme.color.primary)
                ) {
                    Text(text = "Barcha mijozlar")
                }
                Button(
                    onClick = {}, modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppTheme.color.primary)
                ) {
                    Text(text = "Mijozlar qarzorligi")
                }
            }
        }
    }
}

@Composable
fun ReportsCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.report_icon),
                    contentDescription = null,
                    tint = AppTheme.color.primary,
                    modifier = Modifier.padding(end = 8.dp)
                )

                Text(
                    text = "Hisobot",
                    fontSize = 20.sp,
                    style = AppTheme.typography.headlineH3,
                    modifier = Modifier
                )
            }

            HorizontalDivider()


            Row(
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Button(
                    onClick = {}, modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppTheme.color.primary)
                ) {
                    Text(text = "Umumiy hisobot")
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp)
                )
            }
        }
    }
}


@Preview
@Composable
fun MenuScreenPreview() {
    MenuScreen(navController = rememberNavController())
}