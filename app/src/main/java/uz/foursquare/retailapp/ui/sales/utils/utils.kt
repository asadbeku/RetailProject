package uz.foursquare.retailapp.ui.sales.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.foursquare.retailapp.ui.sales.DiscountButton
import uz.foursquare.retailapp.ui.sales.SingleChoiceSegmentedButton
import uz.foursquare.retailapp.ui.theme.AppTheme
import uz.foursquare.retailapp.ui.theme.RetailAppTheme
import uz.foursquare.retailapp.utils.convertToPriceFormat
import java.math.BigDecimal
import java.math.RoundingMode
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import uz.foursquare.retailapp.utils.ui_components.AddButton
import uz.foursquare.retailapp.utils.ui_components.CancelButton
import uz.foursquare.retailapp.utils.ui_components.CustomButton
import uz.foursquare.retailapp.utils.ui_components.CustomOutlinedTextField


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddClientBottomSheetDialog(
    sheetState: SheetState,
    selectedName: String,
    onNameChanged: (String) -> Unit,
    selectedSurname: String,
    onSurnameChanged: (String) -> Unit,
    selectedPhoneNumber: String,
    onPhoneNumberChanged: (String) -> Unit,
    onAddClient: () -> Unit,
    onDismiss: () -> Unit
) {
    RetailAppTheme {

        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = AppTheme.appColor.neutralLightLight
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .navigationBarsPadding()
                    .imePadding()
            ) {

                Text(
                    text = "Mijoz qo'shish",
                    style = AppTheme.typography.headlineH2,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomOutlinedTextField(
                    value = selectedName,
                    onValueChange = { onNameChanged(it) },
                    label = "Ism*",
                    isRequired = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = selectedName,
                    onValueChange = { onNameChanged(it) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = AppTheme.appColor.neutralLightMedium,
                        focusedContainerColor = AppTheme.appColor.neutralLightLight,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = AppTheme.color.primary,
                        cursorColor = AppTheme.color.primary,
                        focusedLabelColor = AppTheme.color.primary
                    ),
                    label = { Text("Mijoz ismini kiriting*") }
                )

                OutlinedTextField(
                    value = selectedSurname,
                    onValueChange = { onSurnameChanged(it) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = AppTheme.appColor.neutralLightMedium,
                        focusedContainerColor = AppTheme.appColor.neutralLightLight,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = AppTheme.color.primary,
                        cursorColor = AppTheme.color.primary,
                        focusedLabelColor = AppTheme.color.primary
                    ),
                    label = { Text("Mijoz familyasini kiriting") }
                )

                OutlinedTextField(
                    value = selectedPhoneNumber,
                    onValueChange = { onPhoneNumberChanged(it) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = AppTheme.appColor.neutralLightMedium,
                        focusedContainerColor = AppTheme.appColor.neutralLightLight,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = AppTheme.color.primary,
                        cursorColor = AppTheme.color.primary,
                        focusedLabelColor = AppTheme.color.primary
                    ),
                    label = { Text("Mijoz telefon raqamini kiriting*") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    CancelButton(modifier = Modifier.weight(1f), onClick = {

                    })

                    AddButton(modifier = Modifier.weight(1f), onClick = {

                    })
                }

            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralDiscountBottomSheet(
    sheetState: SheetState,
    currentPrice: Double,
    onDismiss: (Double) -> Unit
) {

//    var currentPrice by remember { mutableStateOf("120000") }
    var discountPercent by remember { mutableStateOf("") }
    var discountUZS by remember { mutableStateOf("") }
    var selectedDiscountType by remember { mutableIntStateOf(0) }
    val discountRates = listOf("15", "30", "50", "75")
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val originalPrice = currentPrice
    val discountPercentValue = discountPercent.toDoubleOrNull()?.coerceAtMost(100.0) ?: 0.0
    val discountUZSValue = discountUZS.toDoubleOrNull()?.coerceAtMost(originalPrice) ?: 0.0

    val discountedPrice = when (selectedDiscountType) {
        0 -> originalPrice * (1 - discountPercentValue / 100)
        1 -> originalPrice - discountUZSValue
        else -> originalPrice
    }.coerceAtLeast(0.0)

    val displayDiscountPercent = when (selectedDiscountType) {
        0 -> discountPercentValue
        1 -> (discountUZSValue * 100 / originalPrice).let {
            BigDecimal(it).setScale(1, RoundingMode.HALF_UP).toDouble()
        }

        else -> 0.0
    }

    RetailAppTheme {
        Scaffold(containerColor = AppTheme.appColor.neutralLightLight) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp)
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Chekning umumiy narxi",
                        style = AppTheme.typography.headlineH3
                    )

                    Text(
                        text = discountedPrice.convertToPriceFormat(),
                        modifier = Modifier.padding(8.dp),
                        style = AppTheme.typography.headlineH1,
                        overflow = TextOverflow.Ellipsis,
                        color = AppTheme.color.primary,
                        textAlign = TextAlign.Center
                    )

                    if (displayDiscountPercent > 0) {
                        Row {
                            Text(
                                text = originalPrice.convertToPriceFormat(),
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    textDecoration = TextDecoration.LineThrough
                                ),
                                color = AppTheme.appColor.neutralDarkLightest,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "(${displayDiscountPercent}%)",
                                style = TextStyle(fontSize = 18.sp),
                                modifier = Modifier.padding(start = 8.dp),
                                color = AppTheme.appColor.neutralDarkLightest,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = if (selectedDiscountType == 0) discountPercent else discountUZS,
                        onValueChange = {
                            if (selectedDiscountType == 0) discountPercent = it else discountUZS =
                                it
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
                            Text(
                                "Chegirma summasini",
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )
                        }
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    SingleChoiceSegmentedButton(
                        modifier = Modifier.height(52.dp),
                        onSelected = { index -> selectedDiscountType = index }
                    )
                }
                if (selectedDiscountType == 0) {
                    DiscountButton(discountRates, modifier = Modifier.padding(top = 8.dp)) {
                        discountPercent = discountRates[it]
                    }
                }

                Row(modifier = Modifier.padding(top = 8.dp)) {

                    CustomButton(
                        text = "Bekor qilish",
                        isEnabled = true,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        containerColor = AppTheme.appColor.supportWarningDark,
                        contentColor = Color.White,
                        disabledContentColor = Color.Black,
                        disabledContainerColor = AppTheme.appColor.supportWarningMedium,
                        onClick = {
                            onDismiss(0.0)
                        })

                    CustomButton(
                        text = "Qo'shish",
                        isEnabled = true,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                        containerColor = AppTheme.color.primary,
                        contentColor = Color.White,
                        disabledContentColor = Color.Black,
                        disabledContainerColor = AppTheme.color.primary,
                        onClick = {
                            onDismiss(discountedPrice)
                        })

                }

            }
        }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DescriptionBottomSheet(
    sheetState: SheetState,
    onDismiss: (String) -> Unit
) {

    var description by remember { mutableStateOf("") }

    RetailAppTheme {
        Scaffold(
            containerColor = AppTheme.appColor.neutralLightLight
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Text(
                    text = "Buyurtma uchun tavfsif yozing:",
                    style = AppTheme.typography.headlineH3
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Buyurtma tavsifi") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    maxLines = 5,
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = AppTheme.appColor.neutralDarkDarkest,
                        unfocusedTextColor = AppTheme.appColor.neutralDarkMedium,
                        disabledTextColor = AppTheme.appColor.neutralDarkLight,
                        errorTextColor = AppTheme.appColor.supportErrorDark,

                        focusedContainerColor = AppTheme.appColor.highlightLightest,
                        unfocusedContainerColor = AppTheme.appColor.neutralLightLightest,
                        disabledContainerColor = AppTheme.appColor.neutralLightLight,
                        errorContainerColor = AppTheme.appColor.supportErrorLight,

                        cursorColor = AppTheme.appColor.highlightDarkest,
                        errorCursorColor = AppTheme.appColor.supportErrorMedium,

                        selectionColors = TextSelectionColors(
                            handleColor = AppTheme.appColor.highlightMedium,
                            backgroundColor = AppTheme.appColor.highlightLight
                        ),

                        focusedIndicatorColor = AppTheme.appColor.highlightDark,
                        unfocusedIndicatorColor = AppTheme.appColor.neutralLightDark,
                        disabledIndicatorColor = AppTheme.appColor.neutralLightMedium,
                        errorIndicatorColor = AppTheme.appColor.supportErrorDark,

                        focusedLeadingIconColor = AppTheme.appColor.supportSuccessDark,
                        unfocusedLeadingIconColor = AppTheme.appColor.neutralDarkMedium,
                        disabledLeadingIconColor = AppTheme.appColor.neutralDarkLight,
                        errorLeadingIconColor = AppTheme.appColor.supportErrorMedium,

                        focusedTrailingIconColor = AppTheme.appColor.supportSuccessMedium,
                        unfocusedTrailingIconColor = AppTheme.appColor.neutralDarkMedium,
                        disabledTrailingIconColor = AppTheme.appColor.neutralDarkLight,
                        errorTrailingIconColor = AppTheme.appColor.supportErrorMedium,

                        focusedPlaceholderColor = AppTheme.appColor.neutralDarkLight,
                        unfocusedPlaceholderColor = AppTheme.appColor.neutralDarkMedium,
                        disabledPlaceholderColor = AppTheme.appColor.neutralDarkLightest,

                        focusedSupportingTextColor = AppTheme.appColor.supportSuccessDark,
                        unfocusedSupportingTextColor = AppTheme.appColor.neutralDarkMedium,
                        disabledSupportingTextColor = AppTheme.appColor.neutralDarkLight,
                        errorSupportingTextColor = AppTheme.appColor.supportErrorMedium
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Default
                    ),
                    placeholder = {
                        Text("Masalan: 3 dona qora rangli futbolka, yetkazib berish soat 15:00 ga qadar")
                    }
                )

                Row(modifier = Modifier.padding(top = 8.dp)) {

                    CustomButton(
                        text = "Bekor qilish",
                        isEnabled = true,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        containerColor = AppTheme.appColor.supportWarningDark,
                        contentColor = Color.White,
                        disabledContentColor = Color.Black,
                        disabledContainerColor = AppTheme.appColor.supportWarningMedium,
                        onClick = {
                            onDismiss("")
                        })

                    CustomButton(
                        text = "Qo'shish",
                        isEnabled = true,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                        containerColor = AppTheme.color.primary,
                        contentColor = Color.White,
                        disabledContentColor = Color.Black,
                        disabledContainerColor = AppTheme.color.primary,
                        onClick = {
                            onDismiss(description)
                        })

                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ProductCountAndPriceBottomSheetPreview() {
    RetailAppTheme {
        AddClientBottomSheetDialog(
            rememberModalBottomSheetState(),
            selectedName = "",
            onDismiss = {},
            onAddClient = {},
            onNameChanged = { },
            selectedSurname = "",
            onSurnameChanged = {},
            selectedPhoneNumber = "",
            onPhoneNumberChanged = { },
        )
    }
}