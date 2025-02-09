package com.escrow.wazipay.data.network.models.user

import com.escrow.wazipay_admin.data.network.models.user.UserDetailsData
import kotlinx.serialization.Serializable

@Serializable
data class UsersDetailsResponseBody(
    val statusCode: Int,
    val message: String,
    val data: List<UserDetailsData>
)
