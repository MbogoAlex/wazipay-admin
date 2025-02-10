package com.escrow.wazipay_admin

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.escrow.wazipay_admin.ui.screen.auth.LoginViewModel
import com.escrow.wazipay_admin.ui.screen.home.UserDetailsViewModel
import com.escrow.wazipay_admin.ui.screen.home.UsersViewModel
import com.escrow.wazipay_admin.ui.screen.start.SplashViewModel

object AppViewModelFactory {
    @RequiresApi(Build.VERSION_CODES.O)
    val Factory = viewModelFactory {

        initializer {
            SplashViewModel(
                dbRepository = wazipayApplication().container.dbRepository
            )
        }

        initializer {
            LoginViewModel(
                apiRepository = wazipayApplication().container.apiRepository,
                dbRepository = wazipayApplication().container.dbRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }

        initializer {
            UsersViewModel(
                apiRepository = wazipayApplication().container.apiRepository,
                dbRepository = wazipayApplication().container.dbRepository,
            )
        }

        initializer {
            UserDetailsViewModel(
                apiRepository = wazipayApplication().container.apiRepository,
                dbRepository = wazipayApplication().container.dbRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }
    }
}

fun CreationExtras.wazipayApplication(): WazipayAdmin =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as WazipayAdmin)