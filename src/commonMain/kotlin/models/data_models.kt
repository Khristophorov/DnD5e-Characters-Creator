package me.khrys.dnd.charcreator.common.models

import kotlinx.serialization.Serializable

@Serializable
data class User(val _id: String)

@Serializable
data class Translation(val _id: String, val value: String)
