package uz.foursquare.retailapp.ui.sales.search_product

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import co.yml.charts.common.extensions.isNotNull
import kotlinx.coroutines.delay
import uz.foursquare.retailapp.ui.goods.goods_screen.types.GoodType
import uz.foursquare.retailapp.ui.sales.search_product.view_model.SearchViewModel
import uz.foursquare.retailapp.ui.theme.AppTheme
import uz.foursquare.retailapp.ui.theme.RetailAppTheme
import uz.foursquare.retailapp.utils.ui_components.CustomOutlinedTextField
import uz.foursquare.retailapp.utils.ui_components.CustomProductItem
import uz.foursquare.retailapp.utils.ui_components.EmptyListMessage
import uz.foursquare.retailapp.utils.ui_components.LoadingIndicator

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navigation: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel<SearchViewModel>()
) {

    val snackBarHostState = remember { SnackbarHostState() }
    val productList = viewModel.goods.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.errorMessage.collect {
            if (it.isNotNull()) {
                it?.let { snackBarHostState.showSnackbar(it) }
            }
            viewModel.clearError()
        }
    }

    Scaffold(
        containerColor = AppTheme.appColor.neutralLightLight,
        snackbarHost = {
            SnackbarHost(snackBarHostState) { snackBarData ->
                Snackbar(
                    snackbarData = snackBarData,
                    modifier = Modifier,
                    actionOnNewLine = false,
                    shape = RoundedCornerShape(16.dp),
                    containerColor = AppTheme.appColor.supportWarningMedium,
                    contentColor = Color.White,
                    actionColor = Color.Red,
                    actionContentColor = Color.Green,
                    dismissActionContentColor = Color.Blue,
                )

            }
        }
    ) {
        Column {
            SearchSection {
                viewModel.searchByQuery(it)
            }
            if (isLoading.value) {
                LoadingIndicator()
            }
            if (productList.value.isNotEmpty()) {
                ItemsCard(
                    productList.value,
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    viewModel,
                    navigation = navigation
                )
            } else {
                EmptyListMessage("Bunday maxsulot topilmadi...\uD83D\uDE22")
            }

        }
    }
}

@Composable
fun SearchSection(onSearch: (String) -> Unit) {
    var searchText by remember { mutableStateOf("") }
    var isActive by remember { mutableStateOf(false) }

    LaunchedEffect(searchText) {
        delay(500) // Задержка перед отправкой запроса (500 мс)
        onSearch(searchText) // Отправляем запрос после паузы
    }

    Card(
        shape = RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        CustomOutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = "Qidirish...",
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        )
    }
}

@Composable
fun ItemsCard(
    list: List<GoodType>,
    modifier: Modifier,
    viewModel: SearchViewModel,
    navigation: NavHostController
) {
    Card(
        shape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp),
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        LazyColumn {
            itemsIndexed(list) { index, item ->
                CustomProductItem(item, index = index) { position ->
                    val id = viewModel.getIdByPosition(position)
                    navigation.previousBackStackEntry?.savedStateHandle?.set("product_id", id)
                    navigation.popBackStack()
                }
            }

        }
    }
}

@Preview
@Composable
fun SearchScreenPreview(modifier: Modifier = Modifier) {
    RetailAppTheme {
        SearchScreen(rememberNavController())
    }
}