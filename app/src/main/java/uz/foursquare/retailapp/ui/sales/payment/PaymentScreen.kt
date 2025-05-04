package uz.foursquare.retailapp.ui.sales.payment

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uz.foursquare.retailapp.R
import uz.foursquare.retailapp.navigation.home.SalesScreen
import uz.foursquare.retailapp.ui.sales.payment.types.PaymentAmounts
import uz.foursquare.retailapp.ui.sales.payment.types.PaymentMethod
import uz.foursquare.retailapp.ui.sales.payment.types.PaymentOption
import uz.foursquare.retailapp.ui.sales.payment.view_model.PaymentViewModel
import uz.foursquare.retailapp.ui.sales.type.OrderDataType
import uz.foursquare.retailapp.ui.theme.AppTheme
import uz.foursquare.retailapp.ui.theme.RetailAppTheme
import uz.foursquare.retailapp.utils.convertToPriceFormat

// PaymentScreen.kt
@Composable
fun PaymentScreen(
    navController: NavHostController,
    viewModel: PaymentViewModel = hiltViewModel()
) {
    val orderTotal by viewModel.orderTotal.collectAsState()
    val paymentAmounts by viewModel.paymentAmounts.collectAsState()
    val paymentOptions by viewModel.paymentOptions.collectAsState()
    val remainingAmount by viewModel.remainingAmount.collectAsState(0.0)
    val totalPayed = viewModel.paymentAmounts.collectAsState().value.totalPaid

    // Handle back stack data
    val savedStateHandle = navController.previousBackStackEntry?.savedStateHandle

    LaunchedEffect(savedStateHandle) {
        savedStateHandle?.getLiveData<OrderDataType>("product_transaction_data")
            ?.observeForever { orderData ->
                Log.d("TAG", orderData.toString())
                viewModel.setOrderData(orderData)
                viewModel.setOrderTotal(orderData.totalPrice)
                viewModel.updatePaymentAmount(PaymentMethod.CASH, orderData.totalPrice.toFloat())
                savedStateHandle.remove<String>("product_transaction_data")
            }
    }

    RetailAppTheme {
        Scaffold(
            topBar = { PaymentToolbar("To'lov") { navController.navigateUp() } },
            containerColor = AppTheme.appColor.neutralLightLight,
        ) { padding ->
            Column(Modifier.padding(padding)) {
                Column(Modifier.weight(1f)) {
                    PaymentSummarySection(
                        totalAmount = orderTotal,
                        remainingAmount = remainingAmount
                    )

                    PaymentMethodSelection(
                        options = paymentOptions,
                        onSelectionChanged = viewModel::selectPaymentOption
                    )

                    PaymentDetailsSection(
                        totalAmount = orderTotal.toFloat(),
                        paymentOptions = paymentOptions,
                        paymentAmounts = paymentAmounts,
                        onAmountChanged = viewModel::updatePaymentAmount,
                        onRemovePayment = { method ->
                            viewModel.updatePaymentAmount(method, 0.0f)
                            viewModel.selectPaymentOption(method)
                        }
                    )
                }
                PaymentActions(navController, remainingAmount, onPaymentCompleted = {
                    viewModel.completeOrder()
                    navController.navigate(SalesScreen.OrderSuccessScreen.route) {
                        popUpTo(SalesScreen.OrderTransactionScreen.route) { inclusive = true }
                    }
                })
            }
        }
    }
}

@Composable
private fun PaymentSummarySection(
    totalAmount: Double,
    remainingAmount: Double,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(16.dp)) {
            AmountRow(
                label = "Jami",
                amount = totalAmount,
                textStyle = AppTheme.typography.headlineH1,
                labelStyle = AppTheme.typography.headlineH2.copy(fontWeight = FontWeight.SemiBold)
            )

            HorizontalDivider()
            if (remainingAmount >= 0) {
                val textStyle =
                    if (remainingAmount == 0.0) {
                        AppTheme.typography.headlineH1.copy(
                            color = AppTheme.appColor.supportSuccessDark
                        )
                    } else {
                        AppTheme.typography.headlineH1.copy(
                            color = AppTheme.appColor.supportErrorDark
                        )
                    }
                val labelStyle = if (remainingAmount == 0.0) {
                    AppTheme.typography.headlineH2.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.appColor.supportSuccessDark
                    )
                } else {
                    AppTheme.typography.headlineH2.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.appColor.supportErrorDark
                    )
                }
                AmountRow(
                    label = "To'lanishi kerak",
                    amount = remainingAmount,
                    textStyle = textStyle,
                    labelStyle = labelStyle
                )
            } else {
                AmountRow(
                    label = "Qaytim",
                    amount = remainingAmount * -1,
                    textStyle = AppTheme.typography.headlineH1.copy(color = AppTheme.appColor.supportSuccessDark),
                    labelStyle = AppTheme.typography.headlineH2.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.appColor.supportSuccessDark
                    )
                )
            }
        }
    }
}

@Composable
private fun AmountRow(
    label: String,
    amount: Double,
    textStyle: androidx.compose.ui.text.TextStyle,
    labelStyle: androidx.compose.ui.text.TextStyle,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(vertical = 16.dp)
    ) {

        Text(
            text = label,
            modifier = Modifier.weight(1f),
            style = labelStyle
        )
        Text(
            text = amount.convertToPriceFormat(),
            style = textStyle
        )
    }
}

@Composable
private fun PaymentMethodSelection(
    options: List<PaymentOption>,
    onSelectionChanged: (PaymentMethod) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Card(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Text(
                text = "To'lov turini tanlang",
                style = AppTheme.typography.headlineH2,
                modifier = Modifier.padding(16.dp)
            )

            Row(
                modifier = Modifier
                    .horizontalScroll(scrollState)
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                options.forEach { option ->
                    PaymentMethodChip(
                        option = option,
                        onSelected = { onSelectionChanged(option.method) },
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun PaymentMethodChip(
    option: PaymentOption,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = option.isSelected,
        onClick = onSelected,
        modifier = modifier,
        shape = RoundedCornerShape(32.dp),
        border = BorderStroke(0.dp, Color.Transparent),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Color(0xFFEAF2FF),
            labelColor = Color(0xFF006FFD),
            selectedContainerColor = Color(0xFF006FFD),
            selectedLabelColor = Color.White
        ),
        label = { Text(text = option.displayName, modifier = Modifier.padding(8.dp)) },
        leadingIcon = {
            if (option.isSelected) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    )
}


@Composable
private fun PaymentDetailsSection(
    paymentOptions: List<PaymentOption>,
    paymentAmounts: PaymentAmounts,
    totalAmount: Float,
    onAmountChanged: (PaymentMethod, Float) -> Unit,
    onRemovePayment: (PaymentMethod) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedPayments = paymentOptions.filter { it.isSelected }

    if (selectedPayments.isEmpty()) return

    Card(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(16.dp)) {
            LazyColumn {
                items(selectedPayments) { option ->
                    val amount = when (option.method) {
                        PaymentMethod.CASH -> paymentAmounts.cash
                        PaymentMethod.CARD -> paymentAmounts.card
                        PaymentMethod.CREDIT -> paymentAmounts.credit
                    }

                    PaymentDetailItem(
                        method = option.method,
                        displayName = option.displayName,
                        amount = amount,
                        onAmountChanged = { onAmountChanged(option.method, it) },
                        onRemove = { onRemovePayment(option.method) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (option != selectedPayments.last()) {
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun PaymentDetailItem(
    method: PaymentMethod,
    displayName: String,
    amount: Float,
    onAmountChanged: (Float) -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    var amountText by remember { mutableStateOf(amount.toString()) }

    LaunchedEffect(amount) {
        if (amount == 0f) {
            amountText = ""
        } else {
            amountText = amount.toInt().toString()
        }

    }

    Card(
        colors = CardDefaults.cardColors(containerColor = AppTheme.appColor.neutralLightLight),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            // Payment Name
            Text(
                text = displayName,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .width(120.dp),
                style = AppTheme.typography.headlineH3,
                color = AppTheme.appColor.neutralDarkLightest,
                fontWeight = FontWeight.Medium
            )

            // Divider
            VerticalDivider(
                modifier = Modifier
                    .height(24.dp)
                    .width(1.dp),
                color = AppTheme.appColor.neutralDarkLightest
            )

            OutlinedTextField(
                value = amountText,
                onValueChange = { newValue ->
                    if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                        amountText = newValue
                        // Convert to float only for business logic
                        newValue.toFloatOrNull()?.let(onAmountChanged)
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                textStyle = AppTheme.typography.headlineH3.copy(textAlign = TextAlign.End),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                visualTransformation = VisualTransformation.None,
                placeholder = {
                    Text(
                        text = "0",
                        style = AppTheme.typography.headlineH3.copy(
                            color = Color.Gray,
                            textAlign = TextAlign.End
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                },
            )

            // Remove Button
            IconButton(
                onClick = onRemove,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.cancel_icon),
                    contentDescription = "Remove payment",
                    tint = AppTheme.appColor.supportErrorDark
                )
            }
        }
    }
}

@Composable
private fun PaymentActions(
    navController: NavController,
    remainingAmount: Double,
    modifier: Modifier = Modifier,
    onPaymentCompleted: () -> Unit
) {
    val isPaymentComplete = remainingAmount <= 0.01

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    if (isPaymentComplete) {
                        onPaymentCompleted()
                    }
                },
                enabled = isPaymentComplete,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .height(60.dp)
                    .weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppTheme.color.primary,
                    disabledContainerColor = AppTheme.color.primary.copy(alpha = 0.5f)
                )
            ) {
                Text(
                    text = "To'lash",
                    style = AppTheme.typography.headlineH2,
                    color = Color.White
                )
            }
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
            IconButton(onClick = { onNavigationIconClick() }, modifier = Modifier.size(50.dp)) {
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
                    navController.navigate(SalesScreen.OrderSuccessScreen.route) {
                        popUpTo(SalesScreen.OrderTransactionScreen.route) {
                            inclusive = true
                        }
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