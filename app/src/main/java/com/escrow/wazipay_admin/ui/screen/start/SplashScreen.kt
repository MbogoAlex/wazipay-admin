package com.escrow.wazipay_admin.ui.screen.start

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth
import com.escrow.wazipay_admin.AppViewModelFactory
import com.escrow.wazipay_admin.R
import com.escrow.wazipay_admin.ui.nav.AppNavigation
import com.escrow.wazipay_admin.ui.theme.WazipayadminTheme
import kotlinx.coroutines.delay

object SplashScreenDestination: AppNavigation {
    override val title: String = "Splash screen"
    override val route: String = "splash-screen"
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SplashScreenComposable(
    navigateToLoginScreen: () -> Unit,
    navigateToLoginScreenWithArgs: (phoneNumber: String, pin: String) -> Unit,
    navigateToDashboardScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: SplashViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        delay(2000)
        if(!uiState.isLoading && uiState.isNavigating) {
            if(uiState.userDetails.username == null) {
                navigateToLoginScreen()
            } else {
                navigateToDashboardScreen()
            }
            viewModel.stopNavigating()
        }
    }

    Box {
        SplashScreen()
    }
}

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Icon(
            tint = MaterialTheme.colorScheme.onBackground,
            painter = painterResource(id = R.drawable.wazipay_logo),
            contentDescription = null,
            modifier = Modifier
                .size(screenWidth(x = 150.0))
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Text(
            text = "Admin",
            fontSize = screenFontSize(x = 16.0).sp,
            fontWeight = FontWeight.Bold
        )
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    WazipayadminTheme {
        SplashScreen()
    }
}
