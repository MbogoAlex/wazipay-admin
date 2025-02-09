package com.escrow.wazipay_admin.data.network.repository

import com.escrow.wazipay_admin.data.network.models.auth.LoginRequestBody
import com.escrow.wazipay_admin.data.network.models.auth.LoginResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    suspend fun login(
        @Body loginRequestBody: LoginRequestBody
    ): Response<LoginResponseBody>
}