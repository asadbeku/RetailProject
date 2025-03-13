package uz.foursquare.retailapp.ui.transaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uz.foursquare.retailapp.ui.theme.AppTheme
import uz.foursquare.retailapp.ui.theme.RetailAppTheme
import uz.foursquare.retailapp.ui.transaction.type.TransactionType
import uz.foursquare.retailapp.utils.convertToPriceFormat
import uz.foursquare.retailapp.utils.millisToDate
import uz.foursquare.retailapp.utils.millisToTime

val listTransactions = listOf<TransactionType>(
    TransactionType(
        id = 1,
        salesNumber = 100001,
        paymentType = "Naqt to'lov",
        150000,
        1739690629253,
        "Megafon"
    ), TransactionType(
        id = 2,
        salesNumber = 100002,
        paymentType = "Karta orqali",
        1150000,
        1739690629253,
        "Megafon"
    ), TransactionType(
        id = 3,
        salesNumber = 100003,
        paymentType = "Naqt to'lov",
        2150000,
        1739509200000,
        "Megafon"
    ), TransactionType(
        id = 4,
        salesNumber = 100004,
        paymentType = "Karta orqali",
        7150000,
        1739509200000,
        "Megafon"
    ), TransactionType(
        id = 3,
        salesNumber = 100003,
        paymentType = "Naqt to'lov",
        2150000,
        1739509200000,
        "Megafon"
    ), TransactionType(
        id = 4,
        salesNumber = 100004,
        paymentType = "Karta orqali",
        7150000,
        1739606460000,
        "Megafon"
    )
).sortedByDescending { it.paymentDate }

@Composable
fun TransactionsScreen(navController: NavHostController) {

    RetailAppTheme {
        Scaffold(
            topBar = { TransactionsToolbar(title = "Transactions") },
            containerColor = AppTheme.appColor.neutralLightLight
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                TransactionList(listTransactions)
            }
        }
    }
}

@Composable
fun TransactionList(transactions: List<TransactionType>) {
    val groupedTransactions = transactions.groupBy { it.paymentDate }

    LazyColumn {
        groupedTransactions.forEach { (date, transactions) ->
            item {
                Text(
                    text = date.millisToDate(),
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp),
                    style = AppTheme.typography.headlineH4
                )
            }
            items(transactions) { transaction ->
                TransactionItem(transaction)
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: TransactionType) {
    Card(
        modifier = Modifier
            .padding(top = 4.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sotuv #${transaction.salesNumber}",
                    style = AppTheme.typography.headlineH4,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = transaction.paymentDate.millisToTime(),
                    style = AppTheme.typography.headlineH4,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = transaction.paymentSum.toDouble().convertToPriceFormat(),
                    color = AppTheme.color.primary,
                    modifier = Modifier.weight(1f),
                    style = AppTheme.typography.headlineH3
                )
                Text(
                    text = transaction.paymentStore,
                    style = AppTheme.typography.headlineH4,
                    color = AppTheme.color.primary
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsToolbar(
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

@Preview
@Composable
fun TransactionScreenPreview() {
    TransactionsScreen(navController = rememberNavController())
}