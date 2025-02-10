package com.escrow.wazipay_admin.ui.screen.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.escrow.wazipay_admin.ui.nav.AppNavigation
import com.escrow.wazipay_admin.ui.theme.WazipayadminTheme

object DashboardScreenDestination: AppNavigation {
    override val title: String = "Dashboard screen"
    override val route: String = "dashboard-screen"
    val child: String = "child"
    val routeWithChild: String = "$route/{$child}"
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardScreenComposable(
    navigateToLoginScreenWithArgs: (phoneNumber: String, pin: String) -> Unit,
    navigateToUserDetailsScreen: (userId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .safeDrawingPadding()
    ) {
        DashboardScreen(
            navigateToLoginScreenWithArgs = navigateToLoginScreenWithArgs,
            navigateToUserDetailsScreen = navigateToUserDetailsScreen
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardScreen(
    navigateToLoginScreenWithArgs: (phoneNumber: String, pin: String) -> Unit,
    navigateToUserDetailsScreen: (userId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
//            .padding(
//                vertical = screenHeight(x = 16.0),
//                horizontal = screenWidth(x = 16.0)
//            )
    ) {
        UsersScreenComposable(
            navigateToLoginScreenWithArgs = navigateToLoginScreenWithArgs,
            navigateToUserDetailsScreen = navigateToUserDetailsScreen
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardScreenPreview() {
    WazipayadminTheme {
        DashboardScreen(
            navigateToLoginScreenWithArgs = {phoneNumber, pin ->  },
            navigateToUserDetailsScreen = {}
        )
    }
}