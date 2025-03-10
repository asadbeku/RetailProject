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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import uz.foursquare.retailapp.R
import uz.foursquare.retailapp.ui.goods.goods_screen.types.GoodType
import uz.foursquare.retailapp.ui.goods.goods_screen.SearchBar
import uz.foursquare.retailapp.ui.theme.AppTheme
import uz.foursquare.retailapp.ui.theme.RetailAppTheme
import uz.foursquare.retailapp.utils.convertToPriceFormat


@Composable
fun SalesScreen(navController: NavController) {
    RetailAppTheme {
        Scaffold(
            topBar = { SalesToolbar(title = "Sales") },
            containerColor = AppTheme.appColor.neutralLightLight
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Column(modifier = Modifier.weight(1f)) {
                    SalesCardScreen()
                }
                BottomContainer(navController)
            }
        }
    }
}

@Composable
fun SalesCardScreen() {
    Card(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 4.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(modifier = Modifier.padding(4.dp)) {
            SearchBar()
        }
    }
    ProductsScreen()
}

@Composable
fun ProductsScreen() {
//    LazyColumn(modifier = Modifier) {
//        items(goods.size) {
//            ProductItem(goods[it])
//        }
//    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductItem(goodItem: GoodType) {
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
                    .clickable { showBottomSheet = true }
            ) {
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
                    modifier = Modifier
                        .padding(0.dp)
                ) {

                    Text(
                        text = goodItem.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = AppTheme.typography.headlineH4,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        text = goodItem.salePrice.convertToPriceFormat(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = AppTheme.typography.headlineH5,
                        color = AppTheme.color.primary,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        text = "${goodItem.count} ${goodItem.uniteType} / ${goodItem.barcode}",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = AppTheme.typography.bodyS
                    )

                }
            }

            Card(
                modifier = Modifier.padding(end = 16.dp),
                colors = CardDefaults.cardColors(
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
                        modifier = Modifier.padding(start = 4.dp, end = 4.dp),
                        color = AppTheme.color.primary
                    )
                    Text(
                        text = "1",
                        style = AppTheme.typography.headlineH3,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                    )
                    Text(
                        text = "+",
                        style = AppTheme.typography.headlineH1,
                        modifier = Modifier.padding(start = 8.dp, end = 4.dp),
                        color = AppTheme.color.primary
                    )
                }
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState,
                containerColor = AppTheme.appColor.neutralLightLight
            ) {
                // Sheet content
                SheetContent()
            }
        }
    }
}

@Composable
fun SheetContent() {
    val tabs = listOf("Tovar soni", "Chegirma")
    var tabIndex by remember { mutableStateOf(0) }
    val pagerState = rememberPagerState(initialPage = tabIndex) { tabs.size }

    LaunchedEffect(tabIndex) {
        pagerState.animateScrollToPage(tabIndex)
    }
    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            tabIndex = pagerState.currentPage
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Tovar soni va chegirmasi",
            modifier = Modifier.padding(start = 16.dp, end = 4.dp),
            style = AppTheme.typography.headlineH2,
            fontWeight = FontWeight.Bold
        )

        TabRow(
            selectedTabIndex = tabIndex,
            containerColor = AppTheme.appColor.neutralLightLight,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[tabIndex]),
                    color = AppTheme.color.primary
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(text = title) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    icon = {
                        if (index == 0) Icon(
                            painter = painterResource(R.drawable.abacus_icon),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        ) else Icon(
                            painter = painterResource(R.drawable.discount_icon),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    selectedContentColor = AppTheme.color.primary,
                    unselectedContentColor = AppTheme.appColor.neutralDarkDark

                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { index ->
            when (index) {
                0 -> ProductCountSheetScreen()
                1 -> ProductDiscountSheetScreen()
                else -> {}
            }
        }
    }
}

@Composable
fun BottomContainer(navController: NavController) {
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
//                    navController.navigate(Graph.MAIN)
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
    TopAppBar(title = {
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
    ),
        modifier = Modifier.clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
    )
}

@Composable
fun ProductCountSheetScreen() {
    var productCount by remember { mutableStateOf(0) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tovar nomi: ",
                modifier = Modifier.padding(),
                style = AppTheme.typography.headlineH3,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "Tovar nomi 2",
                modifier = Modifier.weight(1f),
                style = AppTheme.typography.headlineH3,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                color = AppTheme.color.primary
            )
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
        ) {
            Text(
                text = "Tovar narxi: ",
                modifier = Modifier.padding(),
                style = AppTheme.typography.headlineH3,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "120 000 so'm",
                modifier = Modifier.weight(1f),
                style = AppTheme.typography.headlineH3,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                color = AppTheme.color.primary
            )
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = AppTheme.appColor.neutralLightLight),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tovar sonini kiriting",
                    style = AppTheme.typography.headlineH3,
                    modifier = Modifier
                        .wrapContentHeight()
                )

                Spacer(modifier = Modifier.width(16.dp))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentWidth()
                        .border(
                            2.dp,
                            AppTheme.appColor.highlightDarkest,
                            RoundedCornerShape(16.dp)
                        )
                        .clip(
                            RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Row {

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
                            modifier = Modifier
                                .weight(1f)
                                .focusRequester(focusRequester),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = AppTheme.appColor.neutralLightLight,
                                focusedContainerColor = AppTheme.appColor.neutralLightLight,
                                unfocusedBorderColor = AppTheme.appColor.neutralLightLight,
                                focusedBorderColor = AppTheme.appColor.neutralLightLight
                            ),
                            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
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

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }
}

@Composable
fun ProductDiscountSheetScreen() {
    var discount by remember { mutableStateOf("0") }
    var discountRates: List<String> = listOf("15%", "30%", "50%", "75%")
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tovar nomi: ",
                modifier = Modifier.padding(),
                style = AppTheme.typography.headlineH3,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "Tovar nomi 2",
                modifier = Modifier.weight(1f),
                style = AppTheme.typography.headlineH3,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                color = AppTheme.color.primary
            )
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
        ) {
            Text(
                text = "Tovar narxi: ",
                modifier = Modifier.padding(),
                style = AppTheme.typography.headlineH3,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "120 000 so'm",
                modifier = Modifier.weight(1f),
                style = AppTheme.typography.headlineH3,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                color = AppTheme.color.primary
            )
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
                shape = RoundedCornerShape(16.dp), // Set corner radius to 16.dp
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
                    Text(
                        "Chegirma summasini",
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            )
            Spacer(modifier = Modifier.padding(end = 8.dp))
            SingleChoiceSegmentedButton(modifier = Modifier.height(52.dp))
        }

        DiscountButton(discountRates, modifier = Modifier.padding(top = 8.dp)) { }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }
}

@Composable
fun DiscountButton(list: List<String>, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        list.forEach { name ->
            FilledTonalButton(
                onClick = onClick, colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = AppTheme.appColor.highlightLightest,
                    contentColor = AppTheme.color.primary
                )
            ) { Text(text = name) }
        }
    }
}

@Composable
fun SingleChoiceSegmentedButton(modifier: Modifier = Modifier) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf("%", "UZS")

    SingleChoiceSegmentedButtonRow {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size,
                    baseShape = RoundedCornerShape(16.dp)
                ),
                onClick = { selectedIndex = index },
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