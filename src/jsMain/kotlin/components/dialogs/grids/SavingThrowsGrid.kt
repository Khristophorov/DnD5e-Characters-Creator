package me.khrys.dnd.charcreator.client.components.dialogs.grids

import kotlinx.html.classes
import me.khrys.dnd.charcreator.client.components.dialogs.SavingThrowsItem
import me.khrys.dnd.charcreator.client.components.inputs.dCenteredBold
import me.khrys.dnd.charcreator.common.CHARISMA_TRANSLATION
import me.khrys.dnd.charcreator.common.CLASS_BORDERED
import me.khrys.dnd.charcreator.common.CONSTITUTION_TRANSLATION
import me.khrys.dnd.charcreator.common.DEXTERITY_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_SAVING_THROWS_TRANSLATION
import me.khrys.dnd.charcreator.common.INTELLIGENCE_TRANSLATION
import me.khrys.dnd.charcreator.common.STRENGTH_TRANSLATION
import me.khrys.dnd.charcreator.common.WISDOM_TRANSLATION
import me.khrys.dnd.charcreator.common.models.Character
import react.RBuilder
import styled.styledDiv

fun RBuilder.dSavingThrowsGrid(
    character: Character,
    translations: Map<String, String>,
    proficiencyBonus: Int
) = styledDiv {
    attrs.classes = setOf(CLASS_BORDERED)
    val savingThrows = character.savingThrows
    val abilities = character.abilities
    dSavingThrowsGridItem(
        SavingThrowsItem(
            label = translations[STRENGTH_TRANSLATION] ?: "",
            value = abilities.strength,
            proficient = savingThrows.strength,
            proficiencyBonus = proficiencyBonus
        )
    )
    dSavingThrowsGridItem(
        SavingThrowsItem(
            label = translations[DEXTERITY_TRANSLATION] ?: "",
            value = abilities.dexterity,
            proficient = savingThrows.dexterity,
            proficiencyBonus = proficiencyBonus
        )
    )
    dSavingThrowsGridItem(
        SavingThrowsItem(
            label = translations[CONSTITUTION_TRANSLATION] ?: "",
            value = abilities.constitution,
            proficient = savingThrows.constitution,
            proficiencyBonus = proficiencyBonus
        )
    )
    dSavingThrowsGridItem(
        SavingThrowsItem(
            label = translations[INTELLIGENCE_TRANSLATION] ?: "",
            value = abilities.intelligence,
            proficient = savingThrows.intelligence,
            proficiencyBonus = proficiencyBonus
        )
    )
    dSavingThrowsGridItem(
        SavingThrowsItem(
            label = translations[WISDOM_TRANSLATION] ?: "",
            value = abilities.wisdom,
            proficient = savingThrows.wisdom,
            proficiencyBonus = proficiencyBonus
        )
    )
    dSavingThrowsGridItem(
        SavingThrowsItem(
            label = translations[CHARISMA_TRANSLATION] ?: "",
            value = abilities.charisma,
            proficient = savingThrows.charisma,
            proficiencyBonus = proficiencyBonus
        )
    )
    dCenteredBold(translations[ENTER_SAVING_THROWS_TRANSLATION] ?: "")
}
