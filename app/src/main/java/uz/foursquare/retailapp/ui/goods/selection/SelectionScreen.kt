package uz.foursquare.retailapp.ui.goods.selection

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ripple
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uz.foursquare.retailapp.ui.goods.selection.view_model.SelectionViewModel
import uz.foursquare.retailapp.ui.theme.AppTheme
import uz.foursquare.retailapp.ui.theme.Nunito
import uz.foursquare.retailapp.ui.theme.RetailAppTheme
import uz.foursquare.retailapp.utils.SelectionType

@Composable
fun SelectionScreen(
    navController: NavHostController,
    screenName: String,
    viewModel: SelectionViewModel = hiltViewModel()
) {
    var shouldShowBottomSheet by remember { mutableStateOf(false) }

    val currentScreen = mapOf(
        "brand" to ("Brend nomi" to SelectionType.BRAND),
        "suppliers" to ("Yetkazib beruvchi" to SelectionType.SUPPLIER),
        "product_unit" to ("O'lchov birligi" to SelectionType.PRODUCT_UNIT),
        "category" to ("Turkum" to SelectionType.CATEGORY)
    )

    val (title, type) = currentScreen[screenName] ?: ("Noma'lum" to SelectionType.BRAND)

    val selectionList by viewModel.selectionList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Load data only when the screen is recomposed
    LaunchedEffect(type) {
        type?.let { viewModel.loadSelectionList(it) }
    }

    RetailAppTheme {
        Scaffold(
            topBar = {
                SelectionToolbar(title, navController = navController) {
                    shouldShowBottomSheet = true
                }
            },
            containerColor = AppTheme.appColor.neutralLightLight
        ) { innerPadding ->

            Card(
                modifier = Modifier
                    .padding(top = innerPadding.calculateTopPadding() + 8.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(0.dp)) {
                    SearchBar(title)

                    when {
                        isLoading -> LoadingIndicator()
                        selectionList.isEmpty() -> EmptyListMessage("${currentScreen[screenName]?.first} mavjud emas, uni qo'shing...")
                        else -> SelectionList(selectionList)
                    }
                }
            }

            if (shouldShowBottomSheet) {
                BottomSheetDialog(
                    shouldShowBottomSheet,
                    type = type ?: SelectionType.BRAND, // Default fallback
                    viewModel = viewModel
                ) {
                    shouldShowBottomSheet = false
                }
            }
        }
    }
}

@Composable
private fun LoadingIndicator() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(color = AppTheme.color.primary)
    }
}

@Composable
private fun EmptyListMessage(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), contentAlignment = Alignment.Center
    ) {
        Text(text = message, textAlign = TextAlign.Center)
    }
}

@Composable
fun SelectionList(items: List<String>) {
    var selectedIndex by remember { mutableStateOf(-1) } // Remember selected index

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemsIndexed(items) { index, item ->
            SelectionNameItem(
                name = item,
                index = index,
                isSelected = index == selectedIndex,
                onClick = { selectedIndex = index }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(screenName: String) {
    var searchValue by remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))  // Rounded corners for the entire search bar
                .background(AppTheme.appColor.neutralLightMedium)      // Background color for the entire search bar
                .padding(8.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color(0xFF2F3036)
            )
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
        }
    }
}

@Composable
fun SelectionNameItem(name: String, index: Int, isSelected: Boolean, onClick: () -> Unit) {
    HorizontalDivider()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick, // Handle item selection
                role = Role.Button
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            name,
            modifier = Modifier.weight(1f), // Take remaining space
            style = AppTheme.typography.headlineH3
        )

        if (isSelected) { // Show check icon for selected item
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = Color.Green
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionToolbar(
    title: String,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onPress: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = { Text(text = title, style = AppTheme.typography.headlineH3) },
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
                        onClick = { navController.popBackStack() },
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
                        onClick = { onPress() },  // Handle "Qo'shish" click
                        indication = ripple(),
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                style = AppTheme.typography.headlineH4,
                color = AppTheme.color.primary)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetDialog(
    showSheet: Boolean,
    viewModel: SelectionViewModel,
    type: SelectionType,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val selectedName: String by viewModel.selectedName.collectAsState()

    if (showSheet) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = onDismiss
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Ushbu sahifadan chiqishni hohlaysizmi?",
                    style = AppTheme.typography.headlineH3,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Agarda chiqarsangiz, ushbu tovar haqdagi ma'lumotlar saqlanmaydi",
                    style = AppTheme.typography.bodyM
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = selectedName,
                    onValueChange = { viewModel.updateSelectedName(it) },  // Corrected state update
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
                    label = { Text("Turkum nomini kiriting...") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppTheme.appColor.supportErrorLight,
                            contentColor = AppTheme.appColor.neutralDarkDarkest
                        )
                    ) {
                        Text("Bekor qilish", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            viewModel.addSelection(type)
                            onDismiss()
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppTheme.color.primary,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Qo'shish", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun SelectionWithoutDescriptionScreenPreview() {
    RetailAppTheme {
        SelectionScreen(navController = rememberNavController(), "Ekran nomi")
    }
}