package uz.foursquare.retailapp.ui.sales.transaction.customer

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
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
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uz.foursquare.retailapp.ui.goods.selection.BottomSheetDialog
import uz.foursquare.retailapp.ui.sales.search_product.SearchSection
import uz.foursquare.retailapp.ui.sales.transaction.customer.type.CustomerType
import uz.foursquare.retailapp.ui.sales.transaction.customer.view_model.CustomerViewModel
import uz.foursquare.retailapp.ui.theme.AppTheme
import uz.foursquare.retailapp.ui.theme.RetailAppTheme
import uz.foursquare.retailapp.utils.convertToPriceFormat

@Composable
fun CustomersScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
//    viewModel: CustomerViewModel = hiltViewModel()
) {
//    val customers = viewModel.customers.collectAsState().value
    var shouldShowBottomSheet by remember { mutableStateOf(false) }
    RetailAppTheme {
        Scaffold(
            topBar = {
                CustomerToolbar(
                    title = "Mijozlar",
                    onNavigationIconClick = { navController.navigateUp() },
                    onActionClick = {
                        shouldShowBottomSheet = true
                    }
                )
            },
            containerColor = AppTheme.appColor.neutralLightLight
        ) { innerPadding ->
            Column(modifier = modifier.padding(innerPadding)) {
                SearchSection {}
                CustomerList(
                    customers = customers,
                    onCustomerSelected = { customer ->

                        Log.d("CustomerViewModel", "Selected customer: $customer")

                        navController.previousBackStackEntry?.savedStateHandle?.set(
                            "selected_customer",
                            customer
                        )
                        navController.popBackStack()
                    }
                )
            }

            if (shouldShowBottomSheet){
//                BottomSheetDialog(shouldShowBottomSheet) { }
            }
        }
    }
}

private val customers = mutableListOf<CustomerType>(
    CustomerType(
        id = "1",
        name = "Asadbek",
        phoneNumber = "+998949357115",
        credit = 100000.0,
        orders = listOf("1", "2", "3")
    ),
    CustomerType(
        id = "2",
        name = "Bekzod",
        phoneNumber = "+998949357115",
        credit = 1200000.0,
        orders = listOf("1", "2", "3")
    ),
    CustomerType(
        id = "3",
        name = "Javohir",
        phoneNumber = "+998949357115",
        credit = 0.0,
        orders = listOf("1", "2", "3")
    ),
    CustomerType(
        id = "4",
        name = "Sherzod",
        phoneNumber = "+998949357115",
        credit = 150.0,
        orders = listOf("1", "2", "3")
    )
)

@Composable
fun CustomerList(
    customers: List<CustomerType>,
    onCustomerSelected: (CustomerType) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        itemsIndexed(customers) { _, customer ->
            CustomerItem(
                customer = customer,
                onClick = { onCustomerSelected(customer) }
            )
        }
    }
}

@Composable
fun CustomerItem(
    customer: CustomerType,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = customer.name,
                    style = AppTheme.typography.headlineH3
                )
                Text(
                    text = customer.phoneNumber,
                    style = AppTheme.typography.bodyS
                )
            }

            val creditText = if (customer.credit != 0.0) {
                "- ${customer.credit.convertToPriceFormat()}"
            } else {
                customer.credit.convertToPriceFormat()
            }

            Text(
                text = creditText,
                style = AppTheme.typography.headlineH3,
                color = AppTheme.color.primary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerToolbar(
    title: String,
    onNavigationIconClick: () -> Unit,
    onActionClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                style = AppTheme.typography.headlineH3
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            titleContentColor = Color.Black
        ),
        modifier = Modifier.clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)),
        navigationIcon = {
            IconButton(
                onClick = onNavigationIconClick,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = AppTheme.color.primary,
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        actions = {
            Text(
                text = "Qo'shish",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clickable(
                        onClick = { onActionClick() },
                        indication = ripple(),
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                style = AppTheme.typography.headlineH4,
                color = AppTheme.color.primary
            )
        }
    )
}

@Preview
@Composable
fun CustomersScreenPreview() {
    RetailAppTheme {
        CustomersScreen(rememberNavController())
    }
}