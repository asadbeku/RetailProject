package uz.foursquare.retailapp.ui.auth.login

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import uz.foursquare.retailapp.R
import uz.foursquare.retailapp.ui.auth.login.view_model.LoginViewModel
import uz.foursquare.retailapp.ui.theme.AppTheme
import uz.foursquare.retailapp.ui.theme.RetailAppTheme

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.errorFlow.collect { message ->
            Log.e("LoginScreen", "Error message: $message")
            snackbarHostState.showSnackbar(message)
        }
    }

    RetailAppTheme {
        Scaffold(
            topBar = { LoginToolbar(title = "Profilga kirish") }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LoginCard(viewModel)

                Spacer(modifier = Modifier.weight(1f))

                // ✅ Snackbar above LoginButton
                SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                    Snackbar(
                        modifier = Modifier
                            .padding(bottom = 8.dp) // Adjust padding above the button
                            .fillMaxWidth(),
                        containerColor = AppTheme.appColor.supportWarningMedium,
                        contentColor = Color.White
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear, // Error icon
                                contentDescription = "Error",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(end = 8.dp)
                            )
                            Text(text = snackbarData.visuals.message)
                        }
                    }
                }

                LoginButton(
                    onClick = { viewModel.login(navController) },
                    isEnabled = uiState.isLoginEnabled
                )
            }
        }
    }
}


@Composable
fun ShowError(message: String) {

}

@Composable
fun LoginCard(viewModel: LoginViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = "Telefon raqamingizni va parolingizni kiriting",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))
            PhoneNumberField(viewModel)

            Spacer(modifier = Modifier.height(16.dp))
            PasswordField(viewModel)

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Parolni unutdingizmi?",
                color = AppTheme.color.primary,
                style = MaterialTheme.typography.bodyMedium
            )
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
            viewModel.updatePhoneNumber(numericRegex.replace(it, "").take(13))
        },
        placeholder = { Text("+998 (XX)-XXX-XX-XX") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth(),
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
        )
    )
}

@Composable
fun PasswordField(viewModel: LoginViewModel) {
    val password by viewModel.password.collectAsState()
    var isPasswordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        onValueChange = { viewModel.updatePassword(it) },
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
        },
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
        )
    )
}

@Composable
fun LoginButton(onClick: () -> Unit, isEnabled: Boolean) {
    Button(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        enabled = isEnabled,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = AppTheme.color.primary,
            contentColor = Color.White,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Text("Login", modifier = Modifier.padding(8.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginToolbar(title: String, onNavigationIconClick: () -> Unit = {}) {
    TopAppBar(
        title = { Text(title, color = MaterialTheme.colorScheme.onSurface) },
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    RetailAppTheme {
        LoginScreen(rememberNavController())
    }
}
