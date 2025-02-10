package com.escrow.wazipay_admin.data.network.repository

import com.escrow.wazipay_admin.data.network.models.auth.LoginRequestBody
import com.escrow.wazipay_admin.data.network.models.auth.LoginResponseBody
import com.escrow.wazipay_admin.data.network.models.user.UserVerificationResponseBody
import com.escrow.wazipay_admin.data.network.models.user.UsersVerificationsResponseBody
import retrofit2.Response
import java.time.LocalDate

interface ApiRepository {
    suspend fun login(
        loginRequestBody: LoginRequestBody
    ): Response<LoginResponseBody>

    //    Get users verifications
    suspend fun getUsersVerifications(
        token: String,
        userId: Int?,
        status: String?,
        startDate: LocalDate,
        endDate: LocalDate,
        page: Int,
        size: Int,
    ): Response<UsersVerificationsResponseBody>

    //    Get user verification
    suspend fun getUserVerification(
        token: String,
        userId: Int,
    ): Response<UserVerificationResponseBody>
}