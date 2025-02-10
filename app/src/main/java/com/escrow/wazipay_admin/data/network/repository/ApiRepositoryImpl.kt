package com.escrow.wazipay_admin.data.network.repository

import com.escrow.wazipay_admin.data.network.models.auth.LoginRequestBody
import com.escrow.wazipay_admin.data.network.models.auth.LoginResponseBody
import com.escrow.wazipay_admin.data.network.models.user.UserVerificationResponseBody
import com.escrow.wazipay_admin.data.network.models.user.UsersVerificationsResponseBody
import retrofit2.Response
import java.time.LocalDate

class ApiRepositoryImpl(private val apiService: ApiService): ApiRepository {
    override suspend fun login(loginRequestBody: LoginRequestBody): Response<LoginResponseBody> =
        apiService.login(loginRequestBody)

    override suspend fun getUsersVerifications(
        token: String,
        userId: Int?,
        status: String?,
        startDate: LocalDate,
        endDate: LocalDate,
        page: Int,
        size: Int
    ): Response<UsersVerificationsResponseBody> = apiService.getUsersVerifications(
        token = "Bearer $token",
        userId = userId,
        status = status,
        startDate = startDate,
        endDate = endDate,
        page = page,
        size = size
    )

    override suspend fun getUserVerification(
        token: String,
        userId: Int
    ): Response<UserVerificationResponseBody> =
        apiService.getUserVerification(
            token = "Bearer $token",
            verificationId = userId
        )

    override suspend fun verifyUser(
        token: String,
        userId: Int
    ): Response<UserVerificationResponseBody> =
        apiService.verifyUser(
            token = "Bearer $token",
            userId = userId
        )
}