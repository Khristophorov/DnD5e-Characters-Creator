package me.khrys.dnd.charcreator.client.components.dialogs

data class SavingThrowsItem(
    val title: String = "",
    val label: String,
    val value: Int,
    val proficient: Boolean,
    val proficiencyBonus: Int,
    val additionalBonus: Int = 0
)
