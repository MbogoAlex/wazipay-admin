package com.escrow.wazipay_admin.data.network.repository

import com.escrow.wazipay_admin.data.network.models.auth.LoginRequestBody
import com.escrow.wazipay_admin.data.network.models.auth.LoginResponseBody
import com.escrow.wazipay_admin.data.network.models.user.UserVerificationResponseBody
import com.escrow.wazipay_admin.data.network.models.user.UsersVerificationsResponseBody
import com.escrow.wazipay_admin.data.network.models.user.UsersVerificationsResponseData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDate

interface ApiService {
    @POST("auth/admin/login")
    suspend fun login(
        @Body loginRequestBody: LoginRequestBody
    ): Response<LoginResponseBody>

//    Get users verifications
    @GET("admin/verification-request")
    suspend fun getUsersVerifications(
        @Header("Authorization") token: String,
        @Query("userId") userId: Int?,
        @Query("status") status: String?,
        @Query("startDate") startDate: LocalDate,
        @Query("endDate") endDate: LocalDate,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<UsersVerificationsResponseBody>

//    Get user verification
    @GET("admin/verification-request/{verificationId}")
    suspend fun getUserVerification(
        @Header("Authorization") token: String,
        @Path("verificationId") verificationId: Int,
    ): Response<UserVerificationResponseBody>
}