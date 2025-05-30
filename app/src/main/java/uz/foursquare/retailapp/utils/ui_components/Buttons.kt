package uz.foursquare.retailapp.utils.ui_components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import uz.foursquare.retailapp.ui.theme.AppTheme

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    isEnabled: Boolean,
    modifier: Modifier,
    containerColor: Color,
    contentColor: Color,
    disabledContainerColor: Color,
    disabledContentColor: Color
) {
    Button(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        enabled = isEnabled,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor
        )
    ) {
        Text(text = text, modifier = Modifier.padding(8.dp))
    }
}

@Composable
fun CancelButton(onClick: () -> Unit, modifier: Modifier) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = AppTheme.appColor.supportErrorLight,
            contentColor = AppTheme.appColor.neutralDarkMedium
        ),
        border = BorderStroke(1.dp, AppTheme.appColor.neutralDarkLightest)
    ) {
        Text("Bekor qilish", fontWeight = FontWeight.Bold)
    }
}

@Composable
fun AddButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = AppTheme.color.primary,
            contentColor = AppTheme.appColor.neutralLightLight
        )
    ) {
        Text("Qo'shish", fontWeight = FontWeight.Bold)
    }
}