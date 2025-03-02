package uz.foursquare.retailapp.ui.goods.transaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uz.foursquare.retailapp.R
import uz.foursquare.retailapp.ui.theme.AppTheme
import uz.foursquare.retailapp.ui.theme.RetailAppTheme

@Composable
fun ProductTransactionScreen(navController: NavHostController) {
    RetailAppTheme {
        Scaffold(
            topBar = { ProductTransactionToolbar(title = "Tranzaksiya № 1860001") },
            containerColor = AppTheme.appColor.neutralLightLight,
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Column(modifier = Modifier.weight(1f)) {
                    Customer()
                    Seller()
                    Discount()
                    Description()
                }
                TransactionBottomContainer(navController)
            }
        }
    }
}

@Composable
fun Customer() {
    Card(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 4.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = AppTheme.color.primary,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Mijoz",
                            style = AppTheme.typography.headlineH2
                        )
                        Text(
                            text = "Asadbek | 150 000 UZS",
                            style = AppTheme.typography.headlineH4,
                            color = AppTheme.color.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                }


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Qo'shish",
                        modifier = Modifier,
                        style = AppTheme.typography.headlineH4,
                        color = AppTheme.color.primary
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = AppTheme.color.primary
                    )
                }
            }
        }

    }
}

@Composable
fun Seller() {
    Card(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 4.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.cash_register_icon),
                        contentDescription = null,
                        tint = AppTheme.color.primary,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(22.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Sotuvchi",
                            style = AppTheme.typography.headlineH2
                        )
                        Text(
                            text = "Salimjonov Sardorbek",
                            style = AppTheme.typography.headlineH4,
                            color = AppTheme.color.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                }


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Qo'shish",
                        modifier = Modifier,
                        style = AppTheme.typography.headlineH4,
                        color = AppTheme.color.primary
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = AppTheme.color.primary
                    )
                }
            }
        }

    }
}

@Composable
fun Discount() {
    Card(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 4.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.discount_icon),
                        contentDescription = null,
                        tint = AppTheme.color.primary,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(22.dp)
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Chegirma",
                            style = AppTheme.typography.headlineH2
                        )
                        Text(
                            text = "25 %",
                            style = AppTheme.typography.headlineH4,
                            color = AppTheme.color.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                }


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Qo'shish",
                        modifier = Modifier,
                        style = AppTheme.typography.headlineH4,
                        color = AppTheme.color.primary
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = AppTheme.color.primary
                    )
                }
            }
        }

    }
}

@Composable
fun Description() {
    Card(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 4.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.description_icon),
                        contentDescription = null,
                        tint = AppTheme.color.primary,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Tavsif",
                            style = AppTheme.typography.headlineH2
                        )
                        Text(
                            text = "Bu yerda tavsif matni bo'ladi...",
                            style = AppTheme.typography.headlineH4,
                            color = AppTheme.color.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                }


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Qo'shish",
                        modifier = Modifier,
                        style = AppTheme.typography.headlineH4,
                        color = AppTheme.color.primary
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = AppTheme.color.primary
                    )
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductTransactionToolbar(
    title: String, onNavigationIconClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(title = {
        Text(
            title,
            color = MaterialTheme.colorScheme.onSurface,
            style = AppTheme.typography.headlineH3
        )
    }, actions = {

    }, colors = TopAppBarDefaults.topAppBarColors(
        containerColor = Color.White, titleContentColor = Color.Black
    ),
        modifier = Modifier.clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)),
        navigationIcon = {
            IconButton(onClick = {}, modifier = Modifier.size(50.dp)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = null,
                    tint = AppTheme.color.primary,
                    modifier = Modifier.size(35.dp)
                )
            }
        }
    )
}

@Composable
fun TransactionBottomContainer(navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 22.dp, end = 22.dp, top = 26.dp)
        ) {
            Text(
                text = "To'lash",
                modifier = Modifier.weight(1f),
                style = AppTheme.typography.headlineH2,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "1 350 000 UZS",
                modifier = Modifier,
                style = AppTheme.typography.headlineH2
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 22.dp, end = 22.dp, top = 16.dp)
        ) {
            Text(
                text = "Chegirma",
                modifier = Modifier.weight(1f),
                style = AppTheme.typography.headlineH2,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "50 000 UZS",
                modifier = Modifier,
                style = AppTheme.typography.headlineH2
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Button(
                onClick = {
//                    navController.navigate(Graph.MAIN)
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .height(60.dp)
                    .weight(1f),
                colors = ButtonColors(
                    containerColor = AppTheme.color.primary,
                    contentColor = Color.White,
                    disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                content = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "To'lash",
                            modifier = Modifier.weight(1f),
                            style = AppTheme.typography.headlineH2
                        )
                        Text(
                            text = "1 350 000 UZS",
                            modifier = Modifier,
                            style = AppTheme.typography.headlineH2
                        )

                    }

                }
            )

        }

    }
}

@Preview
@Composable
fun ProductTransactionScreenPreview() {
    RetailAppTheme {
        ProductTransactionScreen(navController = rememberNavController())
    }
}