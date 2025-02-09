package com.escrow.wazipay_admin.data.network.models.user

import kotlinx.serialization.Serializable

@Serializable
data class UserContactData(
    val id: Int,
    val username: String,
    val phoneNumber: String,
    val email: String
)
