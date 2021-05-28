package me.khrys.dnd.charcreator.client

import kotlin.math.floor

fun computeModifier(ability: Int, proficiencyBonus: Int = 0, proficient: Boolean = false): Int {
    val defaultModifier = floor((ability - 10).toDouble() / 2).toInt()
    return if (proficient) defaultModifier + proficiencyBonus else defaultModifier
}

fun computeProficiencyBonus(level: Int): Int {
    if (level in 1..4) return 2
    if (level in 5..8) return 3
    if (level in 9..12) return 4
    return if (level in 13..16) 5
    else 6
}

fun computePassiveSkill(ability: Int, proficiencyBonus: Int = 0, proficient: Boolean = false): Int {
    return computeModifier(ability, proficiencyBonus, proficient) + 10
}

fun toSignedString(number: Int): String = if (number > 0) "+$number" else number.toString()
