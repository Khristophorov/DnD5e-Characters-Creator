package me.khrys.dnd.charcreator.common.models

import kotlinx.serialization.Serializable

@Serializable
data class User(val _id: String, val characters: List<Character> = emptyList())

@Serializable
data class Translation(val _id: String, val value: String)

@Serializable
data class Character(var name: String, var image: String)

fun emptyCharacter() = Character("", "")
