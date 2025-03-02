package uz.foursquare.retailapp.ui.auth.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uz.foursquare.retailapp.R
import uz.foursquare.retailapp.navigation.Graph
import uz.foursquare.retailapp.navigation.auth.AuthScreen
import uz.foursquare.retailapp.ui.auth.login.view_model.LoginViewModel
import uz.foursquare.retailapp.ui.theme.AppTheme
import uz.foursquare.retailapp.ui.theme.RetailAppTheme

//private val viewModel: LoginViewModel by viewModels()

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    RetailAppTheme {
        Scaffold(
            topBar = { LoginToolbar(title = "Profilga kirish") },
            containerColor = AppTheme.appColor.neutralLightLight
        ) { innerPadding ->
            RetailAppTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        modifier = Modifier.padding(top = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Column(modifier = Modifier.padding(32.dp)) {
                            Text(
                                text = "Telefon raqamingizni va parolingizni kiriting",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                                modifier = Modifier
                            )

                            Column(modifier = Modifier.padding(0.dp)) {

                                PhoneNumberField(viewModel)

                                Spacer(modifier = Modifier.height(16.dp))
                                PasswordField(viewModel)
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Parolni unutdingizmi?",
                                    color = AppTheme.color.primary,
                                )
                            }
                        }

                    }


                    /* Spacer to push the button to the bottom */
                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {
                            navController.navigate(Graph.MAIN) {
                                popUpTo(AuthScreen.Login.route) {
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
                            "Login", modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .wrapContentWidth(align = Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PhoneNumberField(viewModel: LoginViewModel) {
    val phoneNumber by viewModel.phoneNumber.collectAsState()
    val numericRegex = Regex("[^0-9+]")

    OutlinedTextField(
        value = phoneNumber,
        onValueChange = {
            val stripped = numericRegex.replace(it, "").take(13) // Allow max 9 digits
            viewModel.updatePhoneNumber(stripped)
        },
        placeholder = { Text("+998 (XX)-XXX-XX-XX") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun PasswordField(viewModel: LoginViewModel) {
    val password by viewModel.password.collectAsState()
    var isPasswordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        onValueChange = { viewModel.updatePassword(it) }, // ✅ Ensures text input updates the state
        label = { Text("Parol") },
        visualTransformation = if (isPasswordVisible) VisualTransformation.None
        else PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
        trailingIcon = {
            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                Icon(
                    painter = painterResource(
                        if (isPasswordVisible) R.drawable.visibility_off else R.drawable.visibility
                    ),
                    contentDescription = if (isPasswordVisible) "Hide Password" else "Show Password"
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginToolbar(
    title: String, onNavigationIconClick: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                title,
                color = MaterialTheme.colorScheme.onSurface,
                style = AppTheme.typography.headlineH3
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        ),
        modifier = Modifier.clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
    )
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    RetailAppTheme {
        LoginScreen(rememberNavController())
    }
}