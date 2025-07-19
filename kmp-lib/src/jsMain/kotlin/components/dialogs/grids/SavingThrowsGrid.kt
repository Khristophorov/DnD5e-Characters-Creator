package me.khrys.dnd.charcreator.client.components.dialogs.grids

import me.khrys.dnd.charcreator.client.components.dialogs.SavingThrowsItem
import me.khrys.dnd.charcreator.client.components.inputs.CenteredBold
import me.khrys.dnd.charcreator.common.CHARISMA_TRANSLATION
import me.khrys.dnd.charcreator.common.CLASS_BORDERED
import me.khrys.dnd.charcreator.common.CONSTITUTION_TRANSLATION
import me.khrys.dnd.charcreator.common.DEXTERITY_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_SAVING_THROWS_TRANSLATION
import me.khrys.dnd.charcreator.common.INTELLIGENCE_TRANSLATION
import me.khrys.dnd.charcreator.common.STRENGTH_TRANSLATION
import me.khrys.dnd.charcreator.common.WISDOM_TRANSLATION
import react.FC
import react.dom.html.ReactHTML.div
import web.cssom.ClassName

val SavingThrowsGrid = FC<SkillsProps>("SavingThrowsGrid") { props ->
    div {
        className = ClassName(CLASS_BORDERED)
        val savingThrows = props.character.savingThrows
        val abilities = props.character.abilities
        SavingThrowsGridItem {
            this.item = SavingThrowsItem(
                label = props.translations[STRENGTH_TRANSLATION] ?: "",
                value = abilities.strength.value,
                proficient = savingThrows.strength,
                proficiencyBonus = props.proficiencyBonus
            )
        }
        SavingThrowsGridItem {
            this.item = SavingThrowsItem(
                label = props.translations[DEXTERITY_TRANSLATION] ?: "",
                value = abilities.dexterity.value,
                proficient = savingThrows.dexterity,
                proficiencyBonus = props.proficiencyBonus
            )
        }
        SavingThrowsGridItem {
            this.item = SavingThrowsItem(
                label = props.translations[CONSTITUTION_TRANSLATION] ?: "",
                value = abilities.constitution.value,
                proficient = savingThrows.constitution,
                proficiencyBonus = props.proficiencyBonus
            )
        }
        SavingThrowsGridItem {
            this.item = SavingThrowsItem(
                label = props.translations[INTELLIGENCE_TRANSLATION] ?: "",
                value = abilities.intelligence.value,
                proficient = savingThrows.intelligence,
                proficiencyBonus = props.proficiencyBonus
            )
        }
        SavingThrowsGridItem {
            this.item = SavingThrowsItem(
                label = props.translations[WISDOM_TRANSLATION] ?: "",
                value = abilities.wisdom.value,
                proficient = savingThrows.wisdom,
                proficiencyBonus = props.proficiencyBonus
            )
        }
        SavingThrowsGridItem {
            this.item = SavingThrowsItem(
                label = props.translations[CHARISMA_TRANSLATION] ?: "",
                value = abilities.charisma.value,
                proficient = savingThrows.charisma,
                proficiencyBonus = props.proficiencyBonus
            )
        }
        CenteredBold { +(props.translations[ENTER_SAVING_THROWS_TRANSLATION] ?: "") }
    }
}
