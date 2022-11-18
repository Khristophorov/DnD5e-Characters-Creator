package me.khrys.dnd.charcreator.client.components.dialogs.grids

import csstype.ClassName
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

val SavingThrowsGrid = FC<SkillsProps> { props ->
    div {
        className = ClassName(CLASS_BORDERED)
        val savingThrows = props.character.savingThrows
        val abilities = props.character.abilities
        SavingThrowsGridItem {
            this.item = SavingThrowsItem(
                label = props.translations[STRENGTH_TRANSLATION] ?: "",
                value = abilities.strength,
                proficient = savingThrows.strength,
                proficiencyBonus = props.proficiencyBonus
            )
        }
        SavingThrowsGridItem {
            this.item = SavingThrowsItem(
                label = props.translations[DEXTERITY_TRANSLATION] ?: "",
                value = abilities.dexterity,
                proficient = savingThrows.dexterity,
                proficiencyBonus = props.proficiencyBonus
            )
        }
        SavingThrowsGridItem {
            this.item = SavingThrowsItem(
                label = props.translations[CONSTITUTION_TRANSLATION] ?: "",
                value = abilities.constitution,
                proficient = savingThrows.constitution,
                proficiencyBonus = props.proficiencyBonus
            )
        }
        SavingThrowsGridItem {
            this.item = SavingThrowsItem(
                label = props.translations[INTELLIGENCE_TRANSLATION] ?: "",
                value = abilities.intelligence,
                proficient = savingThrows.intelligence,
                proficiencyBonus = props.proficiencyBonus
            )
        }
        SavingThrowsGridItem {
            this.item = SavingThrowsItem(
                label = props.translations[WISDOM_TRANSLATION] ?: "",
                value = abilities.wisdom,
                proficient = savingThrows.wisdom,
                proficiencyBonus = props.proficiencyBonus
            )
        }
        SavingThrowsGridItem {
            this.item = SavingThrowsItem(
                label = props.translations[CHARISMA_TRANSLATION] ?: "",
                value = abilities.charisma,
                proficient = savingThrows.charisma,
                proficiencyBonus = props.proficiencyBonus
            )
        }
        CenteredBold { +(props.translations[ENTER_SAVING_THROWS_TRANSLATION] ?: "") }
    }
}
