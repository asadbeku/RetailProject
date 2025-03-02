package uz.foursquare.retailapp.ui.sales.utils

import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import uz.foursquare.retailapp.ui.theme.RetailAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCountAndPriceBottomSheet(sheetState: SheetState, onDismiss: () -> Unit) {


}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ProductCountAndPriceBottomSheetPreview() {
    RetailAppTheme {
        ProductCountAndPriceBottomSheet(rememberModalBottomSheetState(), {})

    }
}