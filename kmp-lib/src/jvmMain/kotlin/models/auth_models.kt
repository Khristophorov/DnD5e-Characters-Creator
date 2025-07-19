package me.khrys.dnd.charcreator.server.models

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val azp: String?,
    val aud: String?,
    val sub: String?,
    val scope: String?,
    val exp: Long?,
    val expires_in: Int?,
    val email: String,
    val email_verified: String?,
    val access_type: String?
)

@Serializable
data class LoginSession(val username: String)
