package me.khrys.dnd.charcreator.client.components.dialogs.grids

import me.khrys.dnd.charcreator.client.components.inputs.dAbilityBox
import me.khrys.dnd.charcreator.common.CHARISMA_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.CHARISMA_TRANSLATION
import me.khrys.dnd.charcreator.common.CONSTITUTION_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.CONSTITUTION_TRANSLATION
import me.khrys.dnd.charcreator.common.DEXTERITY_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.DEXTERITY_TRANSLATION
import me.khrys.dnd.charcreator.common.INTELLIGENCE_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.INTELLIGENCE_TRANSLATION
import me.khrys.dnd.charcreator.common.STRENGTH_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.STRENGTH_TRANSLATION
import me.khrys.dnd.charcreator.common.WISDOM_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.WISDOM_TRANSLATION
import me.khrys.dnd.charcreator.common.models.Abilities
import react.RBuilder

fun RBuilder.dAbilitiesGrid(
    abilities: Abilities,
    translations: Map<String, String>
) {
    dAbilityBox(
        title = translations[STRENGTH_CONTENT_TRANSLATION] ?: "",
        label = translations[STRENGTH_TRANSLATION] ?: "",
        readOnly = true,
        value = abilities.strength,
        translations = translations
    )
    dAbilityBox(
        title = translations[DEXTERITY_CONTENT_TRANSLATION] ?: "",
        label = translations[DEXTERITY_TRANSLATION] ?: "",
        readOnly = true,
        value = abilities.dexterity,
        translations = translations
    )
    dAbilityBox(
        title = translations[CONSTITUTION_CONTENT_TRANSLATION] ?: "",
        label = translations[CONSTITUTION_TRANSLATION] ?: "",
        readOnly = true,
        value = abilities.constitution,
        translations = translations
    )
    dAbilityBox(
        title = translations[INTELLIGENCE_CONTENT_TRANSLATION] ?: "",
        label = translations[INTELLIGENCE_TRANSLATION] ?: "",
        readOnly = true,
        value = abilities.intelligence,
        translations = translations
    )
    dAbilityBox(
        title = translations[WISDOM_CONTENT_TRANSLATION] ?: "",
        label = translations[WISDOM_TRANSLATION] ?: "",
        readOnly = true,
        value = abilities.wisdom,
        translations = translations
    )
    dAbilityBox(
        title = translations[CHARISMA_CONTENT_TRANSLATION] ?: "",
        label = translations[CHARISMA_TRANSLATION] ?: "",
        readOnly = true,
        value = abilities.charisma,
        translations = translations
    )
}
