package uz.foursquare.retailapp.utils

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import uz.foursquare.retailapp.ui.theme.AppTheme
import uz.foursquare.retailapp.ui.theme.Nunito
import uz.foursquare.retailapp.ui.theme.RetailAppTheme

@Composable
fun SelectionScreen(
    navController: NavHostController, screenName: String
) {

    val currentDestinationContent = mapOf(
        "brand" to listOf("Gucci", "Adidas", "Nike"),
        "suppliers" to listOf(
            "OOO Company Gucci", "OOO Company Adidas", "OOO Company Nike"
        ),
        "product_unit" to listOf("dona", "metr", "kg", "litr"),
        "category" to listOf("Kiyim", "Oziq-ovqat", "Elektronika", "Sport")
    )

    val currentScreenName = when (screenName) {
        "brand" -> {
            "Brend nomi" to currentDestinationContent[screenName]
        }

        "suppliers" -> {
            "Yetkazib beruvchi" to currentDestinationContent[screenName]
        }

        "product_unit" -> {
            "O'lchov birligi" to currentDestinationContent[screenName]
        }

        "category" -> {
            "Turkum" to currentDestinationContent[screenName]
        }

        else -> {
            screenName to listOf("1", "2", "3", "4")
        }
    }

    RetailAppTheme {
        Scaffold(
            topBar = {
                SelectionToolbar(
                    "${currentScreenName.first}ni tanlang", navController = navController
                )
            }, containerColor = AppTheme.appColor.neutralLightLight
        ) { innerPadding ->

            Card(
                modifier = Modifier
                    .padding(top = innerPadding.calculateTopPadding() + 8.dp)
                    .fillMaxWidth(), colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(modifier = Modifier.padding(0.dp)) {
                    SearchBar(currentScreenName.first)

                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(currentScreenName.second!!.size) { index ->
                            SelectionNameItem(currentScreenName.second!![index])
                        }

                    }
                }
            }
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

    HorizontalDivider(modifier = Modifier.padding(top = 0.dp))

    Card(
        Modifier
            .padding(top = 8.dp, bottom = 8.dp)
            .clickable(
                onClick = { /* Handle click here */ },
                indication = ripple(), // Use rememberRipple for ripple effect
                interactionSource = remember { MutableInteractionSource() }, // For managing interactions
                role = Role.Button // Optional: for accessibility
            )
            .fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = AppTheme.color.primary
            )

            Text(
                "Yangi ${screenName.lowercase()} qo'shish",
                style = AppTheme.typography.headlineH3,
                color = AppTheme.color.primary,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }

}

@Composable
fun SelectionNameItem(name: String) {
    HorizontalDivider()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = { /* Handle click here */ },
                indication = ripple(), // Use rememberRipple for ripple effect
                interactionSource = remember { MutableInteractionSource() }, // For managing interactions
                role = Role.Button // Optional: for accessibility
            )
    ) {
        Text(
            name,
            modifier = Modifier
                .padding(top = 8.dp, bottom = 8.dp)
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            style = AppTheme.typography.headlineH3
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionToolbar(
    title: String, modifier: Modifier = Modifier, navController: NavHostController
) {
    CenterAlignedTopAppBar(title = { Text(text = title, style = AppTheme.typography.headlineH3) },
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
        })
}

@Preview
@Composable
fun SelectionWithoutDescriptionScreenPreview() {
    RetailAppTheme {
        SelectionScreen(navController = rememberNavController(), "Ekran nomi")
    }
}