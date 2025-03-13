package uz.foursquare.retailapp.ui.goods.payment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uz.foursquare.retailapp.R
import uz.foursquare.retailapp.ui.theme.AppTheme
import uz.foursquare.retailapp.ui.theme.RetailAppTheme
import uz.foursquare.retailapp.utils.convertToPriceFormat

@Composable
fun PaymentScreen(navController: NavHostController) {
    RetailAppTheme {
        Scaffold(
            topBar = { PaymentToolbar(title = "To'lov") },
            containerColor = AppTheme.appColor.neutralLightLight,
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Column(modifier = Modifier.weight(1f)) {
                    PaymentPrice()
                    PaymentType()
                    PaymentSum()
                }
                PaymentBottomContainer(navController)
            }
        }
    }
}

@Composable
fun PaymentPrice() {
    Card(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 4.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
            ) {
                Text(
                    text = "Jami",
                    modifier = Modifier.weight(1f),
                    style = AppTheme.typography.headlineH2,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "1 950 000",
                    modifier = Modifier,
                    style = AppTheme.typography.headlineH1
                )
                Text(
                    text = "UZS",
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .wrapContentHeight(),
                    style = AppTheme.typography.headlineH3,
                )
            }

            HorizontalDivider()

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
            ) {
                Text(
                    text = "To'lanishi kerak",
                    modifier = Modifier.weight(1f),
                    style = AppTheme.typography.headlineH2,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.appColor.supportErrorDark
                )

                Text(
                    text = "1 950 000",
                    modifier = Modifier,
                    style = AppTheme.typography.headlineH1,
                    color = AppTheme.appColor.supportErrorDark
                )
                Text(
                    text = "UZS",
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .wrapContentHeight(),
                    style = AppTheme.typography.headlineH3,
                    color = AppTheme.appColor.supportErrorDark
                )
            }
        }
    }
}

@Composable
fun PaymentType() {
    val chips = listOf(
        "Naqt to'lov", "Karta orqali", "Qarzga"
    )
    var selectedChip by remember { mutableStateOf(chips.first()) }
    val scrollState = rememberScrollState()
    Card(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 4.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
    ) {
        Column {
            Text(
                text = "To'lov turini tanlang",
                style = AppTheme.typography.headlineH2,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
            )

            Row(
                modifier = Modifier
                    .padding(bottom = 8.dp, start = 8.dp)
                    .horizontalScroll(scrollState)
            ) {
                chips.forEach { chip ->
                    FilterChip(
                        selected = selectedChip == chip,
                        border = BorderStroke(0.dp, Color.Transparent),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color(0xFFEAF2FF),
                            labelColor = Color(0xFF006FFD),
                            selectedContainerColor = Color(0xFF006FFD),
                            selectedLabelColor = Color.White
                        ),
                        onClick = { selectedChip = chip },
                        label = {
                            Text(
                                text = chip,
                                modifier = Modifier.padding(8.dp)
                            )
                        }, // Use `text = chip` for clarity
                        leadingIcon = {
                            if (selectedChip == chip) {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = "Done icon",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize),
                                    tint = Color.White
                                )
                            }
                        },
                        modifier = Modifier
                            .padding(end = 4.dp),
                        shape = RoundedCornerShape(32.dp)
                    )
                }
            }


        }
    }
}

@Composable
fun PaymentSum() {
    val payments =
        mapOf("Naqt to'lov" to "1950000", "Karta orqali" to "1950000", "Qarzga" to "10000")
    Card(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            LazyColumn {
                payments.forEach { (paymentName, paymentSum) ->
                    item {
                        PaymentSumItem(paymentName, paymentSum)
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentSumItem(paymentName: String, paymentSum: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = AppTheme.appColor.neutralLightLight),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = paymentName,
                modifier = Modifier
                    .padding(start = 8.dp, top = 16.dp, bottom = 16.dp)
                    .width(width = 120.dp),
                style = AppTheme.typography.headlineH3,
                color = AppTheme.appColor.neutralDarkLightest,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.padding(start = 8.dp))

            VerticalDivider(
                modifier = Modifier
                    .height(56.dp) // Set the divider height
                    .width(3.dp) // Set the divider thickness
                    .fillMaxHeight(),
                color = AppTheme.appColor.neutralDarkLightest // Set the divider color
            )

            Text(
                text = paymentSum.toDouble().convertToPriceFormat(),
                style = AppTheme.typography.headlineH3,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                textAlign = TextAlign.End
            )

            Icon(
                painter = painterResource(R.drawable.cancel_icon),
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp),
                tint = AppTheme.appColor.supportErrorDark
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentToolbar(
    title: String, onNavigationIconClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
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
fun PaymentBottomContainer(navController: NavController) {

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
                            style = AppTheme.typography.headlineH2,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun PaymentScreenPreview() {
    RetailAppTheme {
        PaymentScreen(navController = rememberNavController())
    }
}