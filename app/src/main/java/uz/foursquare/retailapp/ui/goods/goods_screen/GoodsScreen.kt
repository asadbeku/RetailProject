package uz.foursquare.retailapp.ui.goods.goods_screen

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.collect
import uz.foursquare.retailapp.R
import uz.foursquare.retailapp.navigation.home.AddProductScreens
import uz.foursquare.retailapp.ui.goods.goods_screen.types.GoodType
import uz.foursquare.retailapp.ui.goods.goods_screen.view_model.GoodsViewModel
import uz.foursquare.retailapp.ui.theme.AppTheme
import uz.foursquare.retailapp.ui.theme.Nunito
import uz.foursquare.retailapp.ui.theme.RetailAppTheme
import uz.foursquare.retailapp.utils.convertToPriceFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoodsScreen(
    navController: NavHostController,
    viewModel: GoodsViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // Collect error messages and show a Snackbar
    LaunchedEffect(Unit) {
        viewModel.errorMessage.collect { message ->
            if (message.isNotBlank()) {
                Log.e("GoodsScreen", "Error message: $message")
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(state = rememberTopAppBarState())

    Scaffold(
        topBar = { GoodsToolbar(title = "Goods", scrollBehavior = scrollBehavior) },
        floatingActionButton = { AddGoodButton(navController) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = AppTheme.appColor.neutralLightLight
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            SearchBar()
            Spacer(modifier = Modifier.height(4.dp))
            GoodsCard(viewModel)

            // SnackbarHost to show error messages
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                Snackbar(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    containerColor = AppTheme.appColor.supportWarningMedium,
                    contentColor = Color.White
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Error",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp).padding(end = 8.dp)
                        )
                        Text(text = snackbarData.visuals.message)
                    }
                }
            }
        }
    }
}

@Composable
fun GoodsCard(viewModel: GoodsViewModel) {
    val goods by viewModel.goods.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        ChipsGroup()
        HorizontalDivider()

        if (isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(color = AppTheme.color.primary)
            }
        } else {
            LazyColumn(contentPadding = PaddingValues(vertical = 8.dp)) {
                items(goods) { good ->
                    GoodItem(good)
                }
            }
        }
    }
}

@Composable
fun GoodItem(goodItem: GoodType) {
    Row {
        AsyncImage(
            model = "https://picsum.photos/200",
            contentDescription = "Good image",
            placeholder = painterResource(id = R.drawable.image_placeholder),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = goodItem.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = AppTheme.typography.headlineH3,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = goodItem.salePrice.convertToPriceFormat(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = AppTheme.typography.headlineH4,
                color = AppTheme.color.primary,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "${goodItem.count} ${goodItem.uniteType} / ${goodItem.barcode}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = AppTheme.typography.bodyM
            )
        }
    }
    HorizontalDivider()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar() {
    var searchValue by remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = Color(0xFF2F3036))
        Spacer(modifier = Modifier.width(8.dp))

        BasicTextField(
            value = searchValue,
            onValueChange = { newText -> searchValue = newText },
            modifier = Modifier.weight(1f),
            enabled = true,
            singleLine = true,
        ) { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = searchValue,
                innerTextField = innerTextField,
                singleLine = true,
                enabled = true,
                container = { SelectionContainer {} },
                placeholder = { Text(text = "Qidiruv", fontFamily = Nunito) },
                contentPadding = PaddingValues(4.dp),
                interactionSource = interactionSource,
                visualTransformation = VisualTransformation.None,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.White,
                    disabledIndicatorColor = Color.White,
                    cursorColor = Color.Red
                )
            )
        }
        Spacer(modifier = Modifier.width(8.dp))

        IconButton(onClick = { searchValue = "" }) {
            Icon(
                painter = painterResource(id = R.drawable.barcode_scanner),
                contentDescription = "Clear",
                tint = Color(0xFF2F3036)
            )
        }
    }
}

@Composable
fun ChipsGroup() {
    val chips = listOf("Barchasi (2 300)", "Aktiv (1 100)", "Noaktiv (30)", "Kam qolgan (250)", "Qolmagan (100)")
    var selectedChip by remember { mutableStateOf(chips.first()) }
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .padding(top = 8.dp, start = 8.dp)
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
                label = { Text(text = chip) },
                leadingIcon = {
                    if (selectedChip == chip) {
                        Icon(imageVector = Icons.Filled.Done, contentDescription = "Done", tint = Color.White)
                    }
                },
                modifier = Modifier.padding(end = 4.dp).clip(RoundedCornerShape(12.dp))
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoodsToolbar(title: String, scrollBehavior: TopAppBarScrollBehavior) {
    TopAppBar(
        title = { Text(text = title, style = AppTheme.typography.headlineH3) },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
        actions = {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp).size(30.dp)
            )
        }
    )
}

@Composable
fun AddGoodButton(navController: NavHostController) {
    FloatingActionButton(
        onClick = { navController.navigate(AddProductScreens.AddProduct.route) },
        containerColor = AppTheme.color.primary
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.White)
            Text(text = "Tovar qo'shish", color = Color.White, modifier = Modifier.padding(8.dp))
        }
    }
}


@Preview
@Composable
fun GoodsScreenPreview() {
    val navController = rememberNavController()
    GoodsScreen(navController)
}