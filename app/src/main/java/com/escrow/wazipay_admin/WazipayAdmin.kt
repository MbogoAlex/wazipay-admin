package com.escrow.wazipay_admin

import android.app.Application
import com.escrow.wazipay_admin.container.AppContainer
import com.escrow.wazipay_admin.container.AppContainerImpl

class WazipayAdmin: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl(this)
    }
}