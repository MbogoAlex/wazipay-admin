package com.escrow.wazipay_admin.ui.screen.auth

import android.app.Activity
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.escrow.wazipay.utils.composables.TextFieldComposable
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth
import com.escrow.wazipay_admin.AppViewModelFactory
import com.escrow.wazipay_admin.R
import com.escrow.wazipay_admin.ui.nav.AppNavigation
import com.escrow.wazipay_admin.ui.theme.WazipayadminTheme
import com.escrow.wazipay_admin.ui.utils.composables.PasswordFieldComposable

object LoginScreenDestination: AppNavigation {
    override val title: String = "Login screen"
    override val route: String = "login-screen"
    val phoneNumber: String = "phoneNumber"
    val pin: String = "pin"
    val routeWithArgs: String = "$route/{$phoneNumber}/{$pin}"
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginScreenComposable(
    navigateToDashboardScreen: () -> Unit,
) {
    val context = LocalContext.current
    BackHandler(onBack = {
        (context as Activity).finish()
    })

    val viewModel: LoginViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    when(uiState.loginStatus) {
        LoginStatus.INITIAL -> {}
        LoginStatus.LOADING -> {}
        LoginStatus.SUCCESS -> {
            viewModel.resetStatus()
            Toast.makeText(context, uiState.loginMessage, Toast.LENGTH_LONG).show()
            navigateToDashboardScreen()
        }
        LoginStatus.FAIL -> {
            viewModel.resetStatus()
            Toast.makeText(context, uiState.loginMessage, Toast.LENGTH_LONG).show()
        }
    }

    Box(
        modifier = Modifier
            .safeDrawingPadding()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LoginScreen(
            phoneNumber = uiState.phoneNumber,
            pin = uiState.pin,
            onChangePhoneNumber = {
                viewModel.updatePhoneNumber(it)
                viewModel.enableButton()
            },
            onChangePin = {
                viewModel.updatePin(it)
                viewModel.enableButton()
            },
            loginStatus = uiState.loginStatus,
            buttonEnabled = uiState.buttonEnabled && uiState.loginStatus != LoginStatus.LOADING,
            onLogin = {
                viewModel.loginUser()
            },
        )
    }
}

@Composable
fun LoginScreen(
    phoneNumber: String,
    pin: String,
    onChangePhoneNumber: (phone: String) -> Unit,
    onChangePin: (pin: String) -> Unit,
    loginStatus: LoginStatus,
    buttonEnabled: Boolean,
    onLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = screenWidth(x = 16.0),
                vertical = screenHeight(x = 16.0)
            )
    ) {
        Text(
            text = "Admin login",
            fontWeight = FontWeight.Bold,
            fontSize = screenFontSize(x = 24.0).sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Text(
            text = "Enter your credential to login",
            fontSize = screenFontSize(x = 14.0).sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        TextFieldComposable(
            shape = RoundedCornerShape(screenWidth(x = 20.0)),
            label = "Phone number",
            value = phoneNumber,
            leadingIcon = R.drawable.phone,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            onValueChange = onChangePhoneNumber,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        PasswordFieldComposable(
            label = "Pin",
            value = pin,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.NumberPassword
            ),
            onValueChange = onChangePin,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        TextButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .align(Alignment.Start)
        ) {
            Text(
                text = "Forgot password?",
                fontSize = screenFontSize(x = 14.0).sp
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            enabled = buttonEnabled,
            onClick = onLogin,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if(loginStatus == LoginStatus.LOADING) {
                Text(
                    text = "Loading...",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            } else {
                Text(
                    text = "Login",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    WazipayadminTheme {
        LoginScreen(
            phoneNumber = "",
            pin = "",
            onChangePhoneNumber = {},
            onChangePin = {},
            loginStatus = LoginStatus.INITIAL,
            buttonEnabled = false,
            onLogin = { /*TODO*/ },
        )
    }
}