package uz.foursquare.retailapp.ui.goods.add_product

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import uz.foursquare.retailapp.R
import uz.foursquare.retailapp.navigation.home.AddProductScreens
import uz.foursquare.retailapp.ui.home.HomeScreen
import uz.foursquare.retailapp.ui.theme.AppTheme
import uz.foursquare.retailapp.ui.theme.RetailAppTheme
import uz.foursquare.retailapp.utils.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProduct(navController: NavHostController) {
    var shouldShowBottomSheet by remember { mutableStateOf(false) }
    RetailAppTheme {
        Scaffold(
            topBar = {
                AddProductToolbar(
                    title = "Add Product",
                    modifier = Modifier,
                    scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                    navController = navController
                ) { shouldShowBottomSheet = true }
            }, containerColor = AppTheme.appColor.neutralLightLight
        ) { innerPadding ->
            AddProductScreen(innerPadding = innerPadding, navController)
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
fun AddProductScreen(innerPadding: PaddingValues, navController: NavHostController) {

    LazyColumn(modifier = Modifier.padding(0.dp)) {

        item { AddProductImage(innerPadding) }
        item { ProductCount() }
        item { MainSection(navController) }

        item { PricesSection() }



        item { ProductFeatures(navController) }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductToolbar(
    title: String,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController,
    onBackClick: () -> Unit
) {

    CenterAlignedTopAppBar(title = { Text(text = title, style = AppTheme.typography.headlineH3) },
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
                modifier = Modifier.padding(end = 8.dp),
                style = AppTheme.typography.headlineH4,
                color = AppTheme.color.primary
            )
        })
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
    val sheetState = rememberStandardBottomSheetState()
    val scope = rememberCoroutineScope()

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
fun MainSection(navController: NavHostController) {
    Card(
        modifier = Modifier
            .padding()
            .padding(top = 16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        var productName by remember { mutableStateOf("") }
        var barcode by remember { mutableStateOf("") }
        var article by remember { mutableStateOf("") }
        var productCountType by remember { mutableStateOf("") }
        var isVariate by remember { mutableStateOf(false) }
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

            OutlinedTextField(value = productName,
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
                label = { Text("Tovar nomi") })

            OutlinedTextField(value = article,
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

            OutlinedTextField(value = barcode,
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
                            text = "Dona",
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
fun PricesSection() {

    var receiptPrice by remember { mutableStateOf("") }
    var salePrice by remember { mutableStateOf("") }
    var markupPresent by remember { mutableStateOf("") }
    var wholesalePrice by remember { mutableStateOf("") }
    var isFreePrice by remember { mutableStateOf(false) }
    var productCount by remember { mutableIntStateOf(0) }
    var brandName by remember { mutableStateOf("") }
    var supplierName by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }


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

            OutlinedTextField(value = receiptPrice,
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
                label = { Text("Kelish narxi (USD)") })

            OutlinedTextField(value = receiptPrice,
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
                label = { Text("Sotuv narxi (UZS)") })

            OutlinedTextField(value = receiptPrice,
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

            OutlinedTextField(value = receiptPrice,
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
                            checked = isFreePrice,
                            onCheckedChange = { isFreePrice = !isFreePrice },
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
fun ProductCount() {
    var productCount by remember { mutableIntStateOf(0) }
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
                            value = "",
                            onValueChange = {
                                productCount = it.toIntOrNull() ?: 0
                            },
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
fun ProductFeatures(navController: NavHostController) {

    var brandName by remember { mutableStateOf("") }
    var supplierName by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .padding(top = 16.dp, bottom = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Xususiyatlar",
                style = AppTheme.typography.headlineH2,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            HorizontalDivider()
            Text(
                text = "Quida keltirilgan maxsulot xususiyatlariga doir ma'lumotlarni kiriting",
                style = AppTheme.typography.bodyM,
                modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = AppTheme.appColor.neutralLightLight),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(top = 8.dp)
                    .clickable(
                        onClick = {
                            navController.navigate(AddProductScreens.Selection.route + "/brand")
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
                            text = "Brend nomi",
                            style = AppTheme.typography.bodyL,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .weight(1f) // Maintain weight for potential resizing
                                .wrapContentHeight()
                                .padding(start = 8.dp)
                        )

                        Text(
                            text = "Tanlanmagan",
                            style = AppTheme.typography.headlineH4,
                            modifier = Modifier.padding(end = 4.dp),
                            color = AppTheme.appColor.neutralDarkLightest
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
                colors = CardDefaults.cardColors(containerColor = AppTheme.appColor.neutralLightLight),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(top = 8.dp)
                    .clickable(
                        onClick = {
                            navController.navigate(AddProductScreens.Selection.route + "/suppliers")
                        },
                        indication = ripple(),
                        interactionSource = remember { MutableInteractionSource() },
                        role = Role.Button
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
                            text = "Yetkazib beruvchilar",
                            style = AppTheme.typography.bodyL,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .weight(1f) // Maintain weight for potential resizing
                                .wrapContentHeight()
                                .padding(start = 8.dp)
                        )

                        Text(
                            text = "Tanlanmagan",
                            style = AppTheme.typography.headlineH4,
                            modifier = Modifier.padding(end = 4.dp),
                            color = AppTheme.appColor.neutralDarkLightest
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
                colors = CardDefaults.cardColors(containerColor = AppTheme.appColor.neutralLightLight),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(top = 8.dp)
                    .clickable(
                        onClick = {
                            navController.navigate(AddProductScreens.Selection.route + "/category")
                        },
                        indication = ripple(),
                        interactionSource = remember { MutableInteractionSource() },
                        role = Role.Button
                    )
            ) {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Turkum",
                            style = AppTheme.typography.bodyL,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .weight(1f)
                                .wrapContentHeight()
                                .padding(start = 8.dp)
                        )

                        Text(
                            text = "Tanlanmagan",
                            style = AppTheme.typography.headlineH4,
                            modifier = Modifier.padding(end = 4.dp),
                            color = AppTheme.appColor.neutralDarkLightest
                        )

                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint = AppTheme.color.primary
                        )
                    }
                }
            }

            OutlinedTextField(value = description,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = AppTheme.appColor.neutralLightLight,
                    focusedContainerColor = AppTheme.appColor.neutralLightLight,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = AppTheme.color.primary,
                    cursorColor = AppTheme.color.primary,
                    focusedLabelColor = AppTheme.color.primary
                ),
                label = { Text("Tavsif") })
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