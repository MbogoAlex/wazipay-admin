package com.escrow.wazipay_admin.ui.nav

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.escrow.wazipay_admin.ui.screen.auth.LoginScreenComposable
import com.escrow.wazipay_admin.ui.screen.auth.LoginScreenDestination
import com.escrow.wazipay_admin.ui.screen.home.DashboardScreenComposable
import com.escrow.wazipay_admin.ui.screen.home.DashboardScreenDestination
import com.escrow.wazipay_admin.ui.screen.home.UserDetailsScreenComposable
import com.escrow.wazipay_admin.ui.screen.home.UserDetailsScreenDestination
import com.escrow.wazipay_admin.ui.screen.start.SplashScreenComposable
import com.escrow.wazipay_admin.ui.screen.start.SplashScreenDestination
import kotlin.reflect.typeOf

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(
    navController: NavHostController,
    darkMode: Boolean,
    onSwitchTheme: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = SplashScreenDestination.route,
        modifier = modifier
    ) {
        composable(route = SplashScreenDestination.route) {
            SplashScreenComposable(
                navigateToLoginScreen = {
                    navController.navigate(LoginScreenDestination.route)
                },
                navigateToLoginScreenWithArgs = { phoneNumber, pin ->
                    navController.navigate("${LoginScreenDestination.route}/${phoneNumber}/${pin}")
                },
                navigateToDashboardScreen = {
                    navController.navigate(DashboardScreenDestination.route)
                },
            )
        }
        composable(route = LoginScreenDestination.route) {
            LoginScreenComposable(
                navigateToDashboardScreen = {
                    navController.navigate(DashboardScreenDestination.route)
                }
            )
        }
        composable(
            route = LoginScreenDestination.routeWithArgs,
            arguments = listOf(
                navArgument(LoginScreenDestination.phoneNumber) {
                    type = NavType.StringType
                },
                navArgument(LoginScreenDestination.pin) {
                    type = NavType.StringType
                },
            )
        ) {
            LoginScreenComposable(
                navigateToDashboardScreen = {
                    navController.navigate(DashboardScreenDestination.route)
                }
            )
        }

        composable(DashboardScreenDestination.route) {
            DashboardScreenComposable(
                navigateToLoginScreenWithArgs = {phoneNumber, pin ->
                    navController.navigate("${LoginScreenDestination.route}/${phoneNumber}/${pin}")
                },
                navigateToUserDetailsScreen = {
                    navController.navigate("${UserDetailsScreenDestination.route}/${it}")
                }
            )
        }

        composable(
            UserDetailsScreenDestination.routeWithArgs,
            arguments = listOf(
                navArgument(UserDetailsScreenDestination.userId) {
                    type = NavType.StringType
                }
            )
        ) {
            UserDetailsScreenComposable(
                navigateToPreviousScreen = {
                    navController.navigateUp()
                }
            )
        }
    }
}