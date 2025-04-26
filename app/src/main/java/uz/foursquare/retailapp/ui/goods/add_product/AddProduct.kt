package uz.foursquare.retailapp.ui.goods.add_product

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import co.yml.charts.common.extensions.isNotNull
import kotlinx.coroutines.launch
import uz.foursquare.retailapp.R
import uz.foursquare.retailapp.navigation.home.AddProductScreens
import uz.foursquare.retailapp.ui.goods.add_product.view_model.AddProductViewModel
import uz.foursquare.retailapp.ui.goods.selection.SelectedItem
import uz.foursquare.retailapp.ui.theme.AppTheme
import uz.foursquare.retailapp.ui.theme.RetailAppTheme
import uz.foursquare.retailapp.utils.ui_components.LoadingIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProduct(
    navController: NavHostController,
    viewModel: AddProductViewModel = hiltViewModel()
) {
    var shouldShowBottomSheet by remember { mutableStateOf(false) }
    var snackBarHostState = remember { SnackbarHostState() }
    val isLoading by viewModel.isLoading.collectAsState()
    val isAdded by viewModel.isAdded.collectAsState()
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    var selectionItem by remember { mutableStateOf<SelectedItem?>(null) }

    println(savedStateHandle?.getLiveData<SelectedItem>("selected_item")?.value.toString())

    LaunchedEffect(savedStateHandle) {
        savedStateHandle?.getLiveData<SelectedItem>("selected_item")?.observeForever { selected ->
            selectionItem = selected
            selectionItem?.let {
                when (it.screenName) {
                    "brand" -> viewModel.setBrand(mapOf(it.id to it.name))
                    "suppliers" -> viewModel.setSupplier(mapOf(it.id to it.name))
                    "category" -> viewModel.setCategory(mapOf(it.id to it.name))
                    "product_unit" -> viewModel.setUnit(it.name)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.errorMessage.collect { message ->
            if (message.isNotBlank()) {
                snackBarHostState.showSnackbar(message, duration = SnackbarDuration.Short)

                viewModel.clearError()
            }
        }
    }

    RetailAppTheme {
        Scaffold(
            topBar = {
                AddProductToolbar(
                    title = "Add Product",
                    modifier = Modifier,
                    scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                    navController = navController,
                    viewModel
                ) { shouldShowBottomSheet = true }
            },
            containerColor = AppTheme.appColor.neutralLightLight,
            snackbarHost = {
                SnackbarHost(snackBarHostState) {
                    Snackbar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                        containerColor = AppTheme.appColor.supportWarningMedium,
                        contentColor = Color.White
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Error",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(end = 8.dp)
                            )
                            Text(text = it.visuals.message)
                        }
                    }
                }
            }
        ) { innerPadding ->
            AddProductScreen(innerPadding = innerPadding, navController, viewModel)

            BackHandler {
                shouldShowBottomSheet = true
            }

            if (isLoading) {
                LoadingIndicator()
            }

            if (shouldShowBottomSheet) {
                QuitBottomSheet(
                    navController = navController,
                    sheetStateValue = shouldShowBottomSheet
                ) {
                    shouldShowBottomSheet = false
                }
            }
        }
    }
}

@Composable
fun AddProductScreen(
    innerPadding: PaddingValues,
    navController: NavHostController,
    viewModel: AddProductViewModel
) {
    LazyColumn(modifier = Modifier.padding(0.dp)) {

        item { AddProductImage(innerPadding) }
        item { ProductCount(viewModel) }
        item { MainSection(navController, viewModel) }

        item { PricesSection(viewModel) }

        item { ProductFeatures(navController, viewModel) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductToolbar(
    title: String,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController,
    viewModel: AddProductViewModel,
    onBackClick: () -> Unit
) {

    CenterAlignedTopAppBar(
        title = { Text(text = title, style = AppTheme.typography.headlineH3) },
        scrollBehavior = scrollBehavior,
        modifier = modifier.clip(RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp)),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        ),
        navigationIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .padding(start = 8.dp)
                    .clickable(
                        onClick = { onBackClick() },
                        indication = ripple(),
                        interactionSource = remember { MutableInteractionSource() },
                        role = Role.Button
                    )
            )
        },
        actions = {
            Text(
                text = "Qo'shish",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clickable(
                        onClick = { viewModel.addProduct() },  // Handle "Qo'shish" click
                        indication = ripple(),
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                style = AppTheme.typography.headlineH4,
                color = AppTheme.color.primary)
        }
    )
}

// Remove the QuitBottomSheet function as it's not used correctly in this context

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuitBottomSheet(
    navController: NavHostController,
    sheetStateValue: Boolean,
    onDismiss: () -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberStandardBottomSheetState(skipHiddenState = false)

    showBottomSheet = sheetStateValue
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                onDismiss()
            },
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {

                Text(
                    text = "Ushbu sahifadan chiqishni hohlaysizmi?",
                    style = AppTheme.typography.headlineH3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    textAlign = TextAlign.Center
                )
                Text(text = "Agarda chiqarsangiz, ushbu tovar haqdagi ma'lumotlar saqlanmaydi")
                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Button(
                        onClick = {
                            onDismiss()
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .padding(8.dp)
                            .weight(1f),
                        colors = ButtonColors(
                            containerColor = AppTheme.color.primary,
                            contentColor = Color.White,
                            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Text(
                            "Yo'q", modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .wrapContentWidth(align = Alignment.CenterHorizontally),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = {
                            onDismiss()
                            navController.popBackStack()
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .padding(8.dp)
                            .weight(1f),
                        colors = ButtonColors(
                            containerColor = AppTheme.appColor.supportErrorLight,
                            contentColor = Color.White,
                            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Text(
                            "Ha chiqish", modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .wrapContentWidth(align = Alignment.CenterHorizontally),
                            color = AppTheme.appColor.neutralDarkDarkest,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddProductImage(innerPadding: PaddingValues) {
    Card(
        modifier = Modifier
            .padding(innerPadding)
            .padding(top = 16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Maxsulot suratini qo'shish",
                style = AppTheme.typography.headlineH2,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            HorizontalDivider()
            Text(
                text = "5 tagacha maxsulot suratini yuklash mumkin. Maxsulot surati JPEG, PNG ko'rinishlarida bo'lishi kerak",
                style = AppTheme.typography.bodyM,
                modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
            )

            Row {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(16.dp))
                )

                Card(
                    modifier = Modifier
                        .height(80.dp)
                        .width(80.dp)
                        .padding(start = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = AppTheme.appColor.highlightLightest)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .size(80.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.wrapContentSize()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = AppTheme.color.primary
                            )
                            Text(
                                text = "Rasm qo'shish",
                                color = AppTheme.color.primary,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

            }
        }

    }
}

@Composable
fun MainSection(navController: NavHostController, viewModel: AddProductViewModel) {
    Card(
        modifier = Modifier
            .padding()
            .padding(top = 16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        val productName by viewModel.nameState.collectAsState()
        val barcode by viewModel.barcodeState.collectAsState()
        val article by viewModel.skuState.collectAsState()
        val productCountType by remember { mutableStateOf("") }
        val isVariate by remember { mutableStateOf(false) }
        var productUnit = viewModel.unitState.collectAsState().value.toString()


        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = "Asosiy",
                style = AppTheme.typography.headlineH2,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            HorizontalDivider()
            Text(
                text = "Quida keltirilgan maxsulotgan doir ma'lumotlarni kiriting",
                style = AppTheme.typography.bodyM,
                modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
            )

            OutlinedTextField(
                value = productName,
                onValueChange = { viewModel.setName(it) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp), // Set corner radius to 16.dp
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = AppTheme.appColor.neutralLightLight,
                    focusedContainerColor = AppTheme.appColor.neutralLightLight,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = AppTheme.color.primary,
                    cursorColor = AppTheme.color.primary,
                    focusedLabelColor = AppTheme.color.primary
                ),
                label = { Text("Tovar nomi") })

            OutlinedTextField(
                value = article,
                onValueChange = { viewModel.setSku(it) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp), // Set corner radius to 16.dp
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = AppTheme.appColor.neutralLightLight,
                    focusedContainerColor = AppTheme.appColor.neutralLightLight,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = AppTheme.color.primary,
                    cursorColor = AppTheme.color.primary,
                    focusedLabelColor = AppTheme.color.primary
                ),
                label = { Text("Artikul") },
                trailingIcon = {
                    if (article.isEmpty()) {
                        Text(
                            text = "Yaratish",
                            color = AppTheme.color.primary,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    } else {
                        Icon(imageVector = Icons.Outlined.Clear, contentDescription = null)
                    }
                }
            )

            OutlinedTextField(
                value = barcode,
                onValueChange = { viewModel.setBarcode(it) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp), // Set corner radius to 16.dp
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = AppTheme.appColor.neutralLightLight,
                    focusedContainerColor = AppTheme.appColor.neutralLightLight,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = AppTheme.color.primary,
                    cursorColor = AppTheme.color.primary,
                    focusedLabelColor = AppTheme.color.primary
                ),
                label = { Text("Barkod") },
                trailingIcon = {
                    if (barcode.isEmpty()) {
                        Text(
                            text = "Yaratish",
                            color = AppTheme.color.primary,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    } else {
                        Icon(imageVector = Icons.Outlined.Clear, contentDescription = null)
                    }
                }
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = AppTheme.appColor.neutralLightLight),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(top = 8.dp)
                    .clickable(
                        onClick = {
                            navController.navigate(AddProductScreens.Selection.route + "/product_unit")
                        }, indication = ripple(), // Use rememberRipple for ripple effect
                        interactionSource = remember { MutableInteractionSource() }, // For managing interactions
                        role = Role.Button // Optional: for accessibility
                    )
            ) {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxSize(), // Make the Box fill the card's remaining space
                    contentAlignment = Alignment.Center, // Center content within the Box
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "O'lchov birligi",
                            style = AppTheme.typography.bodyL,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .weight(1f) // Maintain weight for potential resizing
                                .wrapContentHeight()
                                .padding(start = 8.dp)
                        )

                        Text(
                            text = if (productUnit.isNotBlank()) productUnit else "Tanlanmagan",
                            style = AppTheme.typography.headlineH4,
                            modifier = Modifier.padding(end = 4.dp),
                            color = if (productUnit.isBlank()) AppTheme.appColor.neutralDarkLightest else AppTheme.color.primary
                        )

                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint = AppTheme.color.primary
                        )
                    }
                }
            }

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = AppTheme.appColor.neutralLightLight
                ), modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(top = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxSize()
                        .clickable(
                            onClick = { navController.navigate(AddProductScreens.SelectionWithDescription.route) },
                            indication = ripple(), // Use rememberRipple for ripple effect
                            interactionSource = remember { MutableInteractionSource() }, // For managing interactions
                            role = Role.Button // Optional: for accessibility
                        ),
                    contentAlignment = Alignment.Center, // Center content within the Box
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Variativlik",
                            style = AppTheme.typography.bodyL,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .weight(1f) // Maintain weight for potential resizing
                                .wrapContentHeight()
                                .padding(start = 8.dp)
                        )

                        Text(
                            text = "Variativ emas",
                            style = AppTheme.typography.headlineH4,
                            modifier = Modifier.padding(end = 4.dp),
                            color = AppTheme.color.primary
                        )

                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
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
fun PricesSection(viewModel: AddProductViewModel) {

    val purchasePrice by viewModel.purchasePriceState.collectAsState()
    val sellingPrice by viewModel.salePriceState.collectAsState()
    val markupPercentage by remember { mutableStateOf("") }  // Renamed from markupPresent
    val bulkPrice by viewModel.discountPriceState.collectAsState()
    var isFlexiblePrice by remember { mutableStateOf(false) }  // Renamed from isFreePrice
    var productQuantity by remember { mutableIntStateOf(0) }   // Renamed from productCount
    var brand by remember { mutableStateOf("") }          // Renamed from brandName
    var supplier by remember { mutableStateOf("") }       // Renamed from supplierName
    var productCategory by remember { mutableStateOf("") } // Renamed from category
    var productDescription by remember { mutableStateOf("") } // Renamed from description



    Card(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth(), colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = "Asosiy",
                style = AppTheme.typography.headlineH2,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            HorizontalDivider()
            Text(
                text = "Quida keltirilgan maxsulotgan doir ma'lumotlarni kiriting",
                style = AppTheme.typography.bodyM,
                modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
            )

            OutlinedTextField(
                value = purchasePrice,
                onValueChange = { viewModel.setPurchasePrice(it) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp), // Set corner radius to 16.dp
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = AppTheme.appColor.neutralLightLight,
                    focusedContainerColor = AppTheme.appColor.neutralLightLight,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = AppTheme.color.primary,
                    cursorColor = AppTheme.color.primary,
                    focusedLabelColor = AppTheme.color.primary
                ),
                label = { Text("Kelish narxi (USD)") })

            OutlinedTextField(
                value = sellingPrice,
                onValueChange = { viewModel.setSalePrice(it) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp), // Set corner radius to 16.dp
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = AppTheme.appColor.neutralLightLight,
                    focusedContainerColor = AppTheme.appColor.neutralLightLight,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = AppTheme.color.primary,
                    cursorColor = AppTheme.color.primary,
                    focusedLabelColor = AppTheme.color.primary
                ),
                label = { Text("Sotuv narxi (UZS)") })

            OutlinedTextField(
                value = markupPercentage,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp), // Set corner radius to 16.dp
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = AppTheme.appColor.neutralLightLight,
                    focusedContainerColor = AppTheme.appColor.neutralLightLight,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = AppTheme.color.primary,
                    cursorColor = AppTheme.color.primary,
                    focusedLabelColor = AppTheme.color.primary
                ),
                label = { Text("Ustama (%)") })

            OutlinedTextField(
                value = bulkPrice,
                onValueChange = { viewModel.setDiscountPrice(it) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp), // Set corner radius to 16.dp
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = AppTheme.appColor.neutralLightLight,
                    focusedContainerColor = AppTheme.appColor.neutralLightLight,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = AppTheme.color.primary,
                    cursorColor = AppTheme.color.primary,
                    focusedLabelColor = AppTheme.color.primary
                ),
                label = { Text("Ulgurji narx (UZS)") })

            Card(
                colors = CardDefaults.cardColors(containerColor = AppTheme.appColor.neutralLightLight),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .wrapContentWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Row {
                        Text(
                            text = "Erkin narxda sotish",
                            style = AppTheme.typography.bodyL,
                            modifier = Modifier
                                .weight(1f)
                                .wrapContentHeight()
                                .padding(start = 4.dp)
                                .align(Alignment.CenterVertically)
                        )

                        Switch(
                            checked = isFlexiblePrice,
                            onCheckedChange = { isFlexiblePrice = !isFlexiblePrice },
                            colors = SwitchDefaults.colors(
                                checkedBorderColor = AppTheme.color.primary,
                                checkedThumbColor = Color.White,
                                checkedTrackColor = AppTheme.color.primary,
                                uncheckedBorderColor = AppTheme.appColor.neutralLightDarkest,
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = AppTheme.appColor.neutralLightDarkest
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCount(viewModel: AddProductViewModel) {
    val productCount = viewModel.quantityState.collectAsState()
    Card(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth(), colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Tovar soni",
                style = AppTheme.typography.headlineH2,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            HorizontalDivider()
            Text(
                text = "Quida keltirilgan maxsulot sonini kiriting",
                style = AppTheme.typography.bodyM,
                modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = AppTheme.appColor.neutralLightLight),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .wrapContentWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Row {
                        Text(
                            text = "Erkin narxda sotish",
                            style = AppTheme.typography.bodyL,
                            modifier = Modifier
                                .wrapContentHeight()
                                .padding(start = 4.dp)
                                .align(Alignment.CenterVertically)
                        )

                        Spacer(Modifier.width(8.dp)) // Add some spacing between text and divider

                        VerticalDivider(
                            modifier = Modifier
                                .height(56.dp) // Set the divider height
                                .width(3.dp) // Set the divider thickness
                                .fillMaxHeight(),
                            color = AppTheme.appColor.neutralDarkLightest // Set the divider color
                        )

                        Spacer(Modifier.width(8.dp))

                        OutlinedTextField(
                            value = productCount.value,
                            onValueChange = { viewModel.setQuantity(it) },
                            placeholder = {
                                Text(
                                    "0",
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.End
                                )
                            },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = AppTheme.appColor.neutralLightLight,
                                focusedContainerColor = AppTheme.appColor.neutralLightLight,
                                unfocusedBorderColor = AppTheme.appColor.neutralLightLight,
                                focusedBorderColor = AppTheme.appColor.neutralLightLight
                            ),
                            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End)
                        )

                        Text(
                            text = "dona",
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(end = 8.dp),
                            style = AppTheme.typography.bodyM,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.appColor.neutralDarkLight
                        )


                    }
                }
            }

        }

    }
}

@Composable
fun ProductFeatures(navController: NavHostController, viewModel: AddProductViewModel) {
    var brandMap = viewModel.brandState.collectAsState()
    var supplierMap = viewModel.supplierState.collectAsState()
    var categoryMap = viewModel.categoryState.collectAsState()
    var description by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Xususiyatlar", style = AppTheme.typography.headlineH2)
            HorizontalDivider()
            Text(
                text = "Quyida keltirilgan mahsulot xususiyatlariga doir ma'lumotlarni kiriting",
                style = AppTheme.typography.bodyM,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            FeatureCard(navController, "Brend nomi", "brand", brandMap?.value)
            FeatureCard(navController, "Yetkazib beruvchilar", "suppliers", supplierMap?.value)
            FeatureCard(navController, "Turkum", "category", categoryMap?.value)

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = AppTheme.appColor.neutralLightLight,
                    focusedContainerColor = AppTheme.appColor.neutralLightLight,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = AppTheme.color.primary,
                    cursorColor = AppTheme.color.primary
                ),
                label = { Text("Tavsif") }
            )
        }
    }
}

@Composable
fun FeatureCard(
    navController: NavHostController,
    title: String,
    route: String,
    dataMap: Map<String, String>?
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = AppTheme.appColor.neutralLightLight),
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(top = 8.dp)
            .clickable(onClick = { navController.navigate(AddProductScreens.Selection.route + "/$route") })
    ) {
        Box(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title,
                    style = AppTheme.typography.bodyL,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                )
                Text(
                    text = dataMap?.values?.firstOrNull() ?: "Tanlanmagan",
                    style = AppTheme.typography.headlineH4,
                    modifier = Modifier.padding(end = 4.dp),
                    color = if (dataMap.isNullOrEmpty()) AppTheme.appColor.neutralDarkLightest else AppTheme.color.primary
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = AppTheme.color.primary
                )
            }
        }
    }
}

@Preview
@Composable
fun AddProductPreview() {
    RetailAppTheme {
        AddProduct(rememberNavController())
    }
}