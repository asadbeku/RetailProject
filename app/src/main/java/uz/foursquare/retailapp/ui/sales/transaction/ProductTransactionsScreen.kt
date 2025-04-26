package uz.foursquare.retailapp.ui.sales.transaction

import android.util.Log
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Cancel
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import co.yml.charts.common.extensions.isNotNull
import uz.foursquare.retailapp.R
import uz.foursquare.retailapp.navigation.home.SalesScreen
import uz.foursquare.retailapp.ui.sales.transaction.customer.type.CustomerType
import uz.foursquare.retailapp.ui.sales.transaction.view_model.ProductTransactionViewModel
import uz.foursquare.retailapp.ui.sales.type.OrderDataType
import uz.foursquare.retailapp.ui.sales.utils.DescriptionBottomSheet
import uz.foursquare.retailapp.ui.sales.utils.GeneralDiscountBottomSheet
import uz.foursquare.retailapp.ui.theme.AppTheme
import uz.foursquare.retailapp.ui.theme.RetailAppTheme
import uz.foursquare.retailapp.utils.convertToPriceFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductTransactionScreen(
    navController: NavHostController,
    viewModel: ProductTransactionViewModel = hiltViewModel()
) {
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val savedPreviousState = navController.previousBackStackEntry?.savedStateHandle
    val selectedCustomer by viewModel.selectedCustomer.collectAsState()
    var showDiscountSheet by remember { mutableStateOf(false) }
    val discountSheetState = rememberModalBottomSheetState()
    var showDescriptionSheet by remember { mutableStateOf(false) }
    val descriptionSheetState = rememberModalBottomSheetState()
    var discountedPrice = viewModel.discountedPrice.collectAsState().value
    var description = viewModel.description.collectAsState().value
    var originalPrice = viewModel.totalOrderPrice.collectAsState().value
    val transactionNumber by viewModel.transactionNumber.collectAsState()

    val discountRate = if (discountedPrice > 0) {
        100 - (discountedPrice * 100 / originalPrice)
    } else 0.0

    LaunchedEffect(savedStateHandle) {
        val customerKey = "selected_customer"
        savedStateHandle?.getLiveData<CustomerType>(customerKey)
            ?.observeForever { selected ->
                viewModel.setSelectedCustomer(selected)
                savedStateHandle.remove<CustomerType>(customerKey)
            }
    }

    LaunchedEffect(savedPreviousState) {
        val orderKey = "order_data"
        savedPreviousState?.getLiveData<OrderDataType>(orderKey)?.observeForever { orderData ->
            Log.d("TAG", "ProductTransactionScreen: $orderData")
            viewModel.setOrderData(orderData)
            viewModel.setTransactionNumber(orderData.transactionNumber.toString())
            savedPreviousState.remove<String>(orderKey)
        }
    }

    RetailAppTheme {
        Scaffold(
            topBar = {
                ProductTransactionToolbar(
                    title = "Tranzaksiya № ${transactionNumber.toString()}",
                    onNavigationIconClick = { navController.navigateUp() }
                )
            },
            containerColor = AppTheme.appColor.neutralLightLight,
        ) { padding ->
            Column(modifier = Modifier.padding(padding)) {
                Column(modifier = Modifier.weight(1f)) {
                    Customer(
                        selectedCustomer,
                        onCancelCustomerClicked = {
                            viewModel.setSelectedCustomer(null)
                        },
                        onCustomerClick = {
                            navController.navigate(SalesScreen.CustomerScreen.route) {
                                popUpTo(SalesScreen.OrderTransactionScreen.route)
                            }
                        })
                    Seller()
                    Discount(
                        discountRate,
                        onDiscountClick = { showDiscountSheet = true },
                        onCancelDiscountClick = {
                            viewModel.setDiscountedPrice(0.0)
                        }
                    )

                    Description(
                        descriptionText = description,
                        onDescriptionClick = {
                            showDescriptionSheet = true
                        }, onCancelDescriptionClick = {
                            viewModel.setDescription("")
                        }
                    )
                }

                TransactionBottomContainer(
                    orderPrice = originalPrice,
                    discountedPrice = discountedPrice,
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }

    if (showDiscountSheet) {
        ModalBottomSheet(
            onDismissRequest = { showDiscountSheet = false },
            sheetState = discountSheetState,
            containerColor = AppTheme.appColor.neutralLightLight
        ) {
            GeneralDiscountBottomSheet(discountSheetState, originalPrice) {
                viewModel.setDiscountedPrice(it)
                showDiscountSheet = false
            }
        }
    }

    if (showDescriptionSheet) {
        ModalBottomSheet(
            onDismissRequest = { showDescriptionSheet = false },
            sheetState = descriptionSheetState,
            containerColor = AppTheme.appColor.neutralLightLight
        ) {
            DescriptionBottomSheet(descriptionSheetState) {
                viewModel.setDescription(it)
                showDescriptionSheet = false
            }
        }
    }
}


@Composable
fun Customer(
    selectedCustomer: CustomerType? = null,
    onCustomerClick: () -> Unit,
    onCancelCustomerClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 4.dp)
            .fillMaxWidth()
            .clickable {
                onCustomerClick()
            },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
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
                        if (selectedCustomer.isNotNull()) {
                            Text(
                                text = "${selectedCustomer?.name} | ${selectedCustomer?.credit?.convertToPriceFormat()}",
                                style = AppTheme.typography.headlineH4,
                                color = AppTheme.color.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                    }

                }


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (selectedCustomer.isNotNull()) {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = null,
                            tint = AppTheme.appColor.supportErrorDark,
                            modifier = Modifier
                                .clickable {
                                    onCancelCustomerClicked()
                                }
                                .padding(end = 16.dp)
                        )

                    } else {
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


//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.Center
//                ) {
//                    Text(
//                        text = "Qo'shish",
//                        modifier = Modifier,
//                        style = AppTheme.typography.headlineH4,
//                        color = AppTheme.color.primary
//                    )
//                    Icon(
//                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
//                        contentDescription = null,
//                        tint = AppTheme.color.primary
//                    )
//                }
            }
        }

    }
}

@Composable
fun Discount(
    generalDiscountRate: Double,
    onDiscountClick: () -> Unit,
    onCancelDiscountClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 4.dp)
            .fillMaxWidth()
            .clickable {
                onDiscountClick()
            },
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
                            text = "Umumiy chegirma",
                            style = AppTheme.typography.headlineH2
                        )

                        if (generalDiscountRate > 0) {
                            Text(
                                text = "${generalDiscountRate.toInt()}%",
                                style = AppTheme.typography.headlineH4,
                                color = AppTheme.color.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                    }

                }


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    if (generalDiscountRate > 0) {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = null,
                            tint = AppTheme.appColor.supportErrorDark,
                            modifier = Modifier
                                .clickable {
                                    onCancelDiscountClick()
                                }
                                .padding(end = 16.dp)
                        )

                    } else {
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
}

@Composable
fun Description(
    onDescriptionClick: () -> Unit,
    descriptionText: String,
    onCancelDescriptionClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 4.dp)
            .fillMaxWidth()
            .clickable {
                onDescriptionClick()
            },
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
                        if (descriptionText.isNotEmpty()) {
                            Text(
                                text = descriptionText,
                                style = AppTheme.typography.headlineH4,
                                color = AppTheme.color.primary,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                }


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (descriptionText.isNotBlank()) {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = null,
                            tint = AppTheme.appColor.supportErrorDark,
                            modifier = Modifier
                                .clickable {
                                    onCancelDescriptionClick()
                                }
                                .padding(16.dp)
                        )
                    } else {
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductTransactionToolbar(
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
            IconButton(onClick = {
                onNavigationIconClick()
            }, modifier = Modifier.size(50.dp)) {
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
fun TransactionBottomContainer(
    orderPrice: Double,
    discountedPrice: Double,
    viewModel: ProductTransactionViewModel,
    navController: NavController
) {
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
                text = orderPrice.convertToPriceFormat(),
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
                text = if (discountedPrice == 0.0) 0.0.convertToPriceFormat() else (orderPrice - discountedPrice).convertToPriceFormat(),
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
                    val order = viewModel.getOrderData()

                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        "product_transaction_data",
                        order
                    )

                    navController.navigate(SalesScreen.PaymentScreen.route) {
                        popUpTo(SalesScreen.OrderTransactionScreen.route)
                    }
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
                            text = if (discountedPrice == 0.0) orderPrice.convertToPriceFormat() else discountedPrice.convertToPriceFormat(),
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