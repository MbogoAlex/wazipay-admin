package com.escrow.wazipay_admin.container

import com.escrow.wazipay_admin.data.network.repository.ApiRepository
import com.escrow.wazipay_admin.data.room.repository.DBRepository

interface AppContainer {
    val apiRepository: ApiRepository
    val dbRepository: DBRepository
}