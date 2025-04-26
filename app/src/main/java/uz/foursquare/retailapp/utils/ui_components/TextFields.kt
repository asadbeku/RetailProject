package uz.foursquare.retailapp.utils.ui_components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import uz.foursquare.retailapp.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    isRequired: Boolean = false,
    placeholder: String = "",
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    maxLength: Int? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value.takeIf { maxLength == null } ?: value.take(maxLength!!),
            onValueChange = {
                if (maxLength == null || it.length <= maxLength) {
                    onValueChange(it)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            label = label?.let {
                {
                    Text(
                        text = if (isRequired) "$label (majburiy)" else label,
                        style = AppTheme.typography.bodyM
                    )
                }
            },
            placeholder = {
                Text(
                    text = placeholder,
                    style = AppTheme.typography.bodyM.copy(color = AppTheme.appColor.neutralDarkMedium)
                )
            },

            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = isError,
            enabled = enabled,
            readOnly = readOnly,
            singleLine = singleLine,
            maxLines = maxLines,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            visualTransformation = visualTransformation,
            shape = AppTheme.shape.input,
            colors = OutlinedTextFieldDefaults.colors(
                // Text colors
                focusedTextColor = AppTheme.appColor.neutralDarkDarkest,
                unfocusedTextColor = AppTheme.appColor.neutralDarkMedium,
                disabledTextColor = AppTheme.appColor.neutralDarkLight,
                errorTextColor = AppTheme.appColor.supportErrorDark,

                // Container colors
                focusedContainerColor = AppTheme.appColor.neutralLightLightest,
                unfocusedContainerColor = AppTheme.appColor.neutralLightLightest,
                disabledContainerColor = AppTheme.appColor.neutralLightLight,
                errorContainerColor = AppTheme.appColor.supportErrorLight,

                // Cursor and selection
                cursorColor = AppTheme.color.primary,
                errorCursorColor = AppTheme.appColor.supportErrorDark,
                selectionColors = TextSelectionColors(
                    handleColor = AppTheme.color.primary,
                    backgroundColor = AppTheme.color.primary
                ),

                // Border colors
                focusedBorderColor = AppTheme.color.primary,
                unfocusedBorderColor = AppTheme.appColor.neutralLightDark,
                disabledBorderColor = AppTheme.appColor.neutralLightMedium,
                errorBorderColor = AppTheme.appColor.supportErrorDark,

                // Icon colors
                focusedLeadingIconColor = AppTheme.color.primary,
                unfocusedLeadingIconColor = AppTheme.appColor.neutralDarkMedium,
                disabledLeadingIconColor = AppTheme.appColor.neutralDarkLight,
                errorLeadingIconColor = AppTheme.appColor.supportErrorDark,

                focusedTrailingIconColor = AppTheme.color.primary,
                unfocusedTrailingIconColor = AppTheme.appColor.neutralDarkMedium,
                disabledTrailingIconColor = AppTheme.appColor.neutralDarkLight,
                errorTrailingIconColor = AppTheme.appColor.supportErrorDark,

                // Placeholder colors
                focusedPlaceholderColor = AppTheme.appColor.neutralDarkLight,
                unfocusedPlaceholderColor = AppTheme.appColor.neutralDarkMedium,
                disabledPlaceholderColor = AppTheme.appColor.neutralDarkLightest,
            ),
            interactionSource = interactionSource
        )

        // Error message
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = AppTheme.appColor.supportErrorDark,
                style = AppTheme.typography.bodyS,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

        // Character counter
        if (maxLength != null) {
            Text(
                text = "${value.length}/$maxLength",
                color = if (value.length > maxLength) AppTheme.appColor.supportErrorDark
                else AppTheme.appColor.neutralDarkMedium,
                style = AppTheme.typography.bodyS,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp)
                    .wrapContentWidth(Alignment.End)
            )
        }
    }
}

@Composable
private fun ClientTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isRequired: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = AppTheme.appColor.neutralLightMedium,
            focusedContainerColor = AppTheme.appColor.neutralLightLight,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = AppTheme.color.primary,
            cursorColor = AppTheme.color.primary,
            focusedLabelColor = AppTheme.color.primary
        ),
        label = {
            Text(
                text = if (isRequired) "$label (majburiy)" else label,
                style = AppTheme.typography.bodyM
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = keyboardType,
            imeAction = ImeAction.Next
        ),
        singleLine = true
    )
}