package com.escrow.wazipay_admin.data.network.repository

import com.escrow.wazipay_admin.data.network.models.auth.LoginRequestBody
import com.escrow.wazipay_admin.data.network.models.auth.LoginResponseBody
import retrofit2.Response

interface ApiRepository {
    suspend fun login(
        loginRequestBody: LoginRequestBody
    ): Response<LoginResponseBody>
}