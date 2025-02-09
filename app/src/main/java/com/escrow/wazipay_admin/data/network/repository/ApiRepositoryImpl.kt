package com.escrow.wazipay_admin.data.network.repository

import com.escrow.wazipay_admin.data.network.models.auth.LoginRequestBody
import com.escrow.wazipay_admin.data.network.models.auth.LoginResponseBody
import retrofit2.Response

class ApiRepositoryImpl(private val apiService: ApiService): ApiRepository {
    override suspend fun login(loginRequestBody: LoginRequestBody): Response<LoginResponseBody> =
        apiService.login(loginRequestBody)
}