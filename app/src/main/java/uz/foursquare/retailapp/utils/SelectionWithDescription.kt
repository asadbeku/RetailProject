package uz.foursquare.retailapp.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uz.foursquare.retailapp.ui.theme.AppTheme
import uz.foursquare.retailapp.ui.theme.RetailAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionWithDescriptionScreen(
    screenName: String = "O'lchov birligi",
    navController: NavHostController
) {
    RetailAppTheme {
        Scaffold(
            topBar = { SelectionToolbar("${screenName}ni tanlang", navController = navController) },
            containerColor = AppTheme.appColor.neutralLightLight
        ) { innerPadding ->

            var selectedIndex by remember { androidx.compose.runtime.mutableIntStateOf(0) }

            Card(
                modifier = Modifier
                    .padding(top = innerPadding.calculateTopPadding() + 8.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(modifier = Modifier.padding(0.dp)) {

                    SelectionWithDescriptionNameItem(
                        name = "Variativ",
                        description = "Qo'shimcha turlarga ega bo'lgan maxsulot",
                        isSelected = selectedIndex == 1// Check if this item is selected
                    ) {
                        selectedIndex = 1 // Update selected index
                    }

                    HorizontalDivider()

                    SelectionWithDescriptionNameItem(
                        name = "Variativ emas",
                        description = "Qo'shimcha turlarga ega bo'lgan maxsulot",
                        isSelected = selectedIndex == 0 // Check if this item is selected
                    ) {
                        selectedIndex = 0 // Update selected index
                    }
                }
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionWithDescriptionNameItem(
    name: String,
    description: String,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp)
            .clickable(
                onClick = onClick,
                indication = ripple(),
                interactionSource = remember { MutableInteractionSource() },
                role = Role.Button
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) { // Center vertically
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    name,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                    style = AppTheme.typography.headlineH3
                )

                Text(
                    text = description,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                )
            }

            if (isSelected) { // Conditionally show the icon
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = null,
                    tint = AppTheme.color.primary,
                    modifier = Modifier.padding(end = 16.dp)
                )
            }

        }

    }

}

@Preview
@Composable
fun SelectionWithDescriptionScreenPreview() {
    RetailAppTheme {
        SelectionWithDescriptionScreen(navController = rememberNavController())
    }
}