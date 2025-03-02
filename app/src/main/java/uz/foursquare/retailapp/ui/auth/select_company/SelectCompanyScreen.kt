package uz.foursquare.retailapp.ui.auth.select_company

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uz.foursquare.retailapp.navigation.auth.AuthScreen
import uz.foursquare.retailapp.ui.auth.login.LoginToolbar
import uz.foursquare.retailapp.ui.theme.AppTheme
import uz.foursquare.retailapp.ui.theme.RetailAppTheme

@Composable
fun SelectCompanyScreen(navController: NavHostController) {
    RetailAppTheme {
        Scaffold(
            topBar = { LoginToolbar(title = "Tashkilotingizni kiriting") },
            containerColor = AppTheme.appColor.neutralLightLight
        ) { innerPadding ->
            Column(
                modifier = Modifier.padding(
                    bottom = innerPadding.calculateBottomPadding(),
                    top = innerPadding.calculateTopPadding()
                )
            ) {
                Card(
                    modifier = Modifier.padding(top = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {

                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .padding(bottom = 8.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Tashkilot STIR raqamini kiriting:",
                            style = MaterialTheme.typography.labelLarge
                        )
                        StirTextField()
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            text = "Tashkilot nomi:",
                            style = MaterialTheme.typography.labelLarge
                        )
                        Text(
                            text = "Qandaydir tashkilot nomi",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.LightGray,
                            modifier = Modifier
                                .background(color = Color.LightGray)
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        navController.navigate(AuthScreen.Login.route) {
                            popUpTo(AuthScreen.SelectCompany.route) {
                                inclusive = true
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.padding(8.dp),
                    colors = ButtonColors(
                        containerColor = AppTheme.color.primary,
                        contentColor = Color.White,
                        disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Text(
                        text = "Keyingisi", modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                            .wrapContentWidth(align = Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }

}

@Composable
fun StirTextField() {
    var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }

    OutlinedTextField(
        value = "",
        onValueChange = {
            val filteredText = it.filter { char -> char.isDigit() }
            val formattedText = formatText(filteredText)
            textFieldValue = TextFieldValue(
                text = formattedText,
                selection = TextRange(formattedText.length)
            )
        },
        label = { Text("XXX XXX XXX") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )


}

private fun formatText(text: String): String {

    var formattedText = ""

    for (i in text.indices) {
        if (i % 3 == 0 && i != 0) {
            formattedText += " "
        }
        formattedText += text[i]
    }
    return formattedText
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RetailAppTheme {
        SelectCompanyScreen(rememberNavController())
    }

}