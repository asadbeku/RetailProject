package uz.foursquare.retailapp.ui.sales

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import co.yml.charts.common.extensions.isNotNull
import coil.compose.AsyncImage
import io.sentry.protocol.App
import uz.foursquare.retailapp.R
import uz.foursquare.retailapp.navigation.home.SalesScreen
import uz.foursquare.retailapp.ui.sales.type.CartItem
import uz.foursquare.retailapp.ui.sales.view_model.SalesViewModel
import uz.foursquare.retailapp.ui.theme.AppTheme
import uz.foursquare.retailapp.ui.theme.RetailAppTheme
import uz.foursquare.retailapp.utils.convertToPriceFormat
import uz.foursquare.retailapp.utils.ui_components.CustomButton
import uz.foursquare.retailapp.utils.ui_components.LoadingIndicator


@Composable
fun SalesScreen(
    navController: NavHostController,
    viewModel: SalesViewModel = hiltViewModel<SalesViewModel>()
) {
    val productId =
        navController.currentBackStackEntry?.savedStateHandle?.get<String>("product_id")
    val snackBarHostState = remember { SnackbarHostState() }
    val products = viewModel.goods.collectAsState().value ?: emptyList()
    val isLoading = viewModel.isLoading.collectAsState().value

    LaunchedEffect(productId) {
        if (productId.isNotNull()) {
            viewModel.getProductById(productId)
            navController.currentBackStackEntry?.savedStateHandle?.remove<String>("product_id")
        }
    }

    LaunchedEffect(Unit) {
        viewModel.errorMessage.collect { message ->
            if (message.isNotNull() && message?.isNotBlank() == true) {
                snackBarHostState.showSnackbar(message)
            }
            viewModel.clearErrorMessage()
        }
    }

    RetailAppTheme {
        Scaffold(
            topBar = { SalesToolbar(title = "Sales") },
            containerColor = AppTheme.appColor.neutralLightLight
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Column(modifier = Modifier.weight(1f)) {
                    SearchCardScreen(navController)
                    ProductsScreen(products, isLoading, viewModel)
                }
                Column {
                    SnackbarHost(snackBarHostState) { snackBarData ->
                        Snackbar(
                            snackBarData, modifier = Modifier,
                            actionOnNewLine = false,
                            shape = RoundedCornerShape(16.dp),
                            containerColor = AppTheme.appColor.supportWarningMedium,
                            contentColor = Color.White,
                            actionColor = Color.Red,
                            actionContentColor = Color.Green,
                            dismissActionContentColor = Color.Blue,
                        )
                    }
                    BottomContainer(navController, viewModel)
                }
            }
        }
    }
}

@Composable
fun SearchCardScreen(navigation: NavHostController) {
    Card(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 4.dp)
            .fillMaxWidth()
            .clickable {
                navigation.navigate(SalesScreen.SearchScreen.route)
            }, colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(
                start = 12.dp,
                end = 12.dp,
                top = 16.dp,
                bottom = 16.dp
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = "Qidirish")
            }
        }
    }
}

@Composable
fun ProductsScreen(products: List<CartItem>, isLoading: Boolean, viewModel: SalesViewModel) {

    if (isLoading) {
        LoadingIndicator()
    }

    LazyColumn(modifier = Modifier) {
        itemsIndexed(products) { index, item ->
            ProductItem(
                products[index],
                index,
                onIncrement = { viewModel.incrementQuantity(index) },
                onDecrement = { viewModel.decrementQuantity(index) }
            )
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductItem(
    goodItem: CartItem,
    index: Int,
    onIncrement: (Int) -> Unit,
    onDecrement: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(top = 4.dp, bottom = 4.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        var showBottomSheet by remember { mutableStateOf(false) }
        val sheetState = rememberModalBottomSheetState()
        Row(verticalAlignment = Alignment.CenterVertically) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .clickable { showBottomSheet = true }) {
                AsyncImage(
                    model = "https://images.unsplash.com/photo-1503602642458-232111445657?q=80&w=2574&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    contentDescription = "Good image",
                    placeholder = painterResource(id = R.drawable.image_placeholder),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(90.dp)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(16.dp))
                )

                Column(
                    modifier = Modifier.padding(0.dp)
                ) {

                    Text(
                        text = goodItem.product.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = AppTheme.typography.headlineH4,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        text = goodItem.product.salePrice.convertToPriceFormat(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = AppTheme.typography.headlineH5,
                        color = AppTheme.color.primary,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        text = "${goodItem.product.count} ${goodItem.product.uniteType} / ${goodItem.product.barcode}",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = AppTheme.typography.bodyS
                    )

                }
            }

            Card(
                modifier = Modifier.padding(end = 16.dp), colors = CardDefaults.cardColors(
                    containerColor = AppTheme.appColor.neutralLightLight
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = "-",
                        style = AppTheme.typography.headlineH1,
                        modifier = Modifier
                            .padding(start = 4.dp, end = 4.dp)
                            .clickable {
                                onDecrement(index)
                            },
                        color = AppTheme.color.primary
                    )
                    Text(
                        text = goodItem.quantity.toString(),
                        style = AppTheme.typography.headlineH3,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                    )
                    Text(
                        text = "+",
                        style = AppTheme.typography.headlineH1,
                        modifier = Modifier
                            .padding(start = 8.dp, end = 4.dp)
                            .clickable {
                                onIncrement(index)
                            },
                        color = AppTheme.color.primary
                    )
                }
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                }, sheetState = sheetState, containerColor = AppTheme.appColor.neutralLightLight
            ) {
                // Sheet content
                ProductQuantityAndDiscountSheetContent(
                    product = goodItem,
                    onConfirm = { quantity, discount ->
                        // Apply quantity and discount
                    },
                    onDismiss = { /* close modal */ }
                )
            }
        }
    }
}

@Composable
fun ProductQuantityAndDiscountSheetContent(
    product: CartItem,
    onConfirm: (quantity: Int, discount: Float) -> Unit,
    onDismiss: () -> Unit
) {
    val tabs = listOf("Tovar soni", "Chegirma")
    var tabIndex by remember { mutableStateOf(0) }
    val pagerState = rememberPagerState(initialPage = tabIndex) { tabs.size }

    var productCount by rememberSaveable { mutableIntStateOf(product.quantity) }
    var discountSum by rememberSaveable { mutableFloatStateOf(0f) }

    LaunchedEffect(tabIndex) {
        pagerState.animateScrollToPage(tabIndex)
    }

    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            tabIndex = pagerState.currentPage
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        SheetHeader(title = "Tovar soni va chegirmasi", onDismiss = onDismiss)

        TabRow(
            selectedTabIndex = tabIndex,
            containerColor = AppTheme.appColor.neutralLightLight,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[tabIndex]),
                    color = AppTheme.color.primary
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    text = { Text(title) },
                    icon = {
                        Icon(
                            painter = painterResource(
                                if (index == 0) R.drawable.abacus_icon
                                else R.drawable.discount_icon
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            when (page) {
                0 -> ProductCountSheetScreen(
                    productName = product.product.name,
                    productPrice = product.product.salePrice.convertToPriceFormat(),
                    initialCount = 1,
                    onCountChange = { updatedCount ->
                        println(updatedCount)
                    }
                )

                1 -> ProductDiscountSheetScreen(
                    productName = product.product.name,
                    productPrice = product.product.salePrice.convertToPriceFormat(),
                    initialDiscount = "0",
                    onDiscountChange = { updatedDiscount ->
                        // Handle discount change here
                        println(updatedDiscount)
                    }
                )
            }
        }

        CustomButton(
            text = "Tasdiqlash",
            onClick = {
                onConfirm(productCount, discountSum)
            },
            isEnabled = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            containerColor = AppTheme.color.primary,
            contentColor = AppTheme.appColor.neutralLightLight,
            disabledContainerColor = AppTheme.appColor.neutralLightLight,
            disabledContentColor = AppTheme.color.primary
        )
    }
}

@Composable
fun SheetHeader(title: String, onDismiss: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = AppTheme.typography.headlineH2,
            fontWeight = FontWeight.Bold
        )
        IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Yopish")
        }
    }
}


@Composable
fun BottomContainer(navController: NavController, viewModel: SalesViewModel) {
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
                onClick = {},
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .height(50.dp)
                    .weight(1f),
                colors = ButtonColors(
                    containerColor = AppTheme.appColor.neutralLightLight,
                    contentColor = Color.White,
                    disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                content = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.barcode_scanner),
                            contentDescription = null,
                            tint = AppTheme.color.primary,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(text = "Skannerlash", color = AppTheme.appColor.neutralDarkDark)
                    }

                }
            )

            Button(
                onClick = {
                    viewModel.getOrderData()?.let { data ->
                        if (data.products.isEmpty()) {
                            viewModel.showError("Tovar tanlanmagan...")
                        } else {
                            println(data)
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                "order_data",
                                data
                            )
                            navController.navigate(SalesScreen.OrderTransactionScreen.route) {
                                popUpTo(SalesScreen.CartScreen.route)
                            }
                        }

                    }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .height(50.dp)
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
                        Text(text = "Keyingisi", modifier = Modifier.padding(end = 8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null
                        )
                    }

                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesToolbar(
    title: String, onNavigationIconClick: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                title,
                color = MaterialTheme.colorScheme.onSurface,
                style = AppTheme.typography.headlineH3
            )
        }, actions = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = "Back",
                    modifier = Modifier.size(30.dp)
                )
            }
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White, titleContentColor = Color.Black
        ), modifier = Modifier.clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
    )
}

@Composable
fun ProductCountSheetScreen(
    productName: String,
    productPrice: String,
    initialCount: Int = 0,
    onCountChange: (Int) -> Unit
) {
    var productCount by remember { mutableStateOf(initialCount.toString()) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Product Name Row
        InfoRow(label = "Tovar nomi:", value = productName)

        // Product Price Row
        InfoRow(label = "Tovar narxi:", value = productPrice)

        // Input card for quantity
        Card(
            colors = CardDefaults.cardColors(containerColor = AppTheme.appColor.neutralLightLight),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Tovar sonini kiriting",
                    style = AppTheme.typography.headlineH3
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            2.dp,
                            AppTheme.appColor.highlightDarkest,
                            RoundedCornerShape(16.dp)
                        )
                        .clip(RoundedCornerShape(16.dp))
                        .padding(horizontal = 12.dp)
                ) {
                    OutlinedTextField(
                        value = productCount,
                        onValueChange = {
                            productCount = it.filter { char -> char.isDigit() }
                            onCountChange(productCount.toIntOrNull() ?: 0)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequester),
                        placeholder = {
                            Text("0", textAlign = TextAlign.End)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent
                        ),
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "dona",
                        style = AppTheme.typography.bodyM,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.appColor.neutralDarkLight
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = AppTheme.typography.headlineH3,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = value,
            style = AppTheme.typography.headlineH3,
            fontWeight = FontWeight.Bold,
            color = AppTheme.color.primary,
            modifier = Modifier.weight(1f),
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun ProductDiscountSheetScreen(
    productName: String = "Tovar nomi 2",
    productPrice: String = "120 000 so'm",
    initialDiscount: String = "0",
    onDiscountChange: (String) -> Unit
) {
    var discount by remember { mutableStateOf(initialDiscount) }
    val discountRates = listOf("15", "30", "50", "75")
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Product Name Row
        InfoRow(label = "Tovar nomi:", value = productName)

        // Product Price Row
        InfoRow(label = "Tovar narxi:", value = productPrice)

        // Discount Input Field
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            OutlinedTextField(
                value = discount,
                onValueChange = { newValue ->
                    discount = newValue.filter { it.isDigit() }
                    onDiscountChange(discount)
                },
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = AppTheme.appColor.neutralLightLight,
                    focusedContainerColor = AppTheme.appColor.neutralLightLight,
                    unfocusedIndicatorColor = AppTheme.appColor.neutralDarkLightest,
                    focusedIndicatorColor = AppTheme.color.primary,
                    cursorColor = AppTheme.color.primary,
                    focusedLabelColor = AppTheme.color.primary
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = {
                    Text("Chegirma summasini", overflow = TextOverflow.Ellipsis, maxLines = 1)
                }
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Discount segmented button
            SingleChoiceSegmentedButton(
                modifier = Modifier.height(52.dp),
                onSelected = { selectedRate ->
                    discount = selectedRate.toString()
                    onDiscountChange(discount)
                }
            )
        }

        // Discount Preset Buttons
        DiscountButton(discountRates, modifier = Modifier.padding(top = 8.dp)) { selectedDiscount ->
            discount = selectedDiscount.toString()
            onDiscountChange(discount)
        }
    }

    // Focus and show keyboard
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }
}


@Composable
fun DiscountButton(
    rates: List<String>,
    modifier: Modifier = Modifier,
    onRateClick: (Int) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        rates.forEachIndexed { index, rate ->
            FilledTonalButton(
                onClick = { onRateClick(index) },
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = AppTheme.appColor.highlightLightest,
                    contentColor = AppTheme.color.primary
                )
            ) {
                Text(text = "$rate%")
            }
        }
    }
}

@Composable
fun SingleChoiceSegmentedButton(modifier: Modifier = Modifier, onSelected: (Int) -> Unit) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf("%", "UZS")

    SingleChoiceSegmentedButtonRow {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index, count = options.size, baseShape = RoundedCornerShape(16.dp)
                ),
                onClick = {
                    selectedIndex = index
                    onSelected(selectedIndex)
                },
                selected = index == selectedIndex,
                label = { Text(label) },
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = AppTheme.color.primary,
                    activeBorderColor = AppTheme.color.primary,
                    activeContentColor = Color.White,
                    inactiveContainerColor = AppTheme.appColor.neutralLightLight,
                    inactiveBorderColor = AppTheme.appColor.neutralDarkLightest,
                    inactiveContentColor = AppTheme.appColor.neutralDarkDark
                ),
                modifier = modifier,
            )
        }
    }
}

@Preview
@Composable
fun SalesScreenPreview() {
    SalesScreen(navController = rememberNavController())
}