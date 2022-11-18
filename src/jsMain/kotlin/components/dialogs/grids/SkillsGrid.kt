package me.khrys.dnd.charcreator.client.components.dialogs.grids

import csstype.ClassName
import me.khrys.dnd.charcreator.client.components.dialogs.SavingThrowsItem
import me.khrys.dnd.charcreator.client.components.inputs.CenteredBold
import me.khrys.dnd.charcreator.common.ACROBATICS_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.ACROBATICS_TRANSLATION
import me.khrys.dnd.charcreator.common.ANIMAL_HANDLING_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.ANIMAL_HANDLING_TRANSLATION
import me.khrys.dnd.charcreator.common.ARCANA_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.ARCANA_TRANSLATION
import me.khrys.dnd.charcreator.common.ATHLETICS_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.ATHLETICS_TRANSLATION
import me.khrys.dnd.charcreator.common.CLASS_BORDERED
import me.khrys.dnd.charcreator.common.DECEPTION_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.DECEPTION_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_SKILLS_TRANSLATION
import me.khrys.dnd.charcreator.common.HISTORY_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.HISTORY_TRANSLATION
import me.khrys.dnd.charcreator.common.INSIGHT_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.INSIGHT_TRANSLATION
import me.khrys.dnd.charcreator.common.INTIMIDATION_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.INTIMIDATION_TRANSLATION
import me.khrys.dnd.charcreator.common.INVESTIGATION_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.INVESTIGATION_TRANSLATION
import me.khrys.dnd.charcreator.common.MEDICINE_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.MEDICINE_TRANSLATION
import me.khrys.dnd.charcreator.common.NATURE_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.NATURE_TRANSLATION
import me.khrys.dnd.charcreator.common.PERCEPTION_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.PERCEPTION_TRANSLATION
import me.khrys.dnd.charcreator.common.PERFORMANCE_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.PERFORMANCE_TRANSLATION
import me.khrys.dnd.charcreator.common.PERSUASION_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.PERSUASION_TRANSLATION
import me.khrys.dnd.charcreator.common.RELIGION_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.RELIGION_TRANSLATION
import me.khrys.dnd.charcreator.common.SLEIGHT_OF_HANDS_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.SLEIGHT_OF_HANDS_TRANSLATION
import me.khrys.dnd.charcreator.common.STEALTH_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.STEALTH_TRANSLATION
import me.khrys.dnd.charcreator.common.SURVIVAL_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.SURVIVAL_TRANSLATION
import me.khrys.dnd.charcreator.common.models.Character
import react.FC
import react.Props
import react.dom.html.ReactHTML.div

external interface SkillsProps : Props {
    var character: Character
    var translations: Map<String, String>
    var proficiencyBonus: Int
}

private external interface SavingThrowsProps : Props {
    var elements: List<SavingThrowsItem>
}

val SkillsGrid = FC<SkillsProps> { props ->
    div {
        this.className = ClassName(CLASS_BORDERED)
        val abilities = props.character.abilities
        val skills = props.character.skills
        SavingThrowsElements {
            this.elements = listOf(
                props.translations[ACROBATICS_TRANSLATION] to
                        SavingThrowsItem(
                            title = props.translations[ACROBATICS_CONTENT_TRANSLATION] ?: "",
                            label = props.translations[ACROBATICS_TRANSLATION] ?: "",
                            value = abilities.dexterity,
                            proficient = skills.acrobatics,
                            proficiencyBonus = props.proficiencyBonus
                        ), props.translations[ANIMAL_HANDLING_TRANSLATION] to
                        SavingThrowsItem(
                            title = props.translations[ANIMAL_HANDLING_CONTENT_TRANSLATION] ?: "",
                            label = props.translations[ANIMAL_HANDLING_TRANSLATION] ?: "",
                            value = abilities.wisdom,
                            proficient = skills.animalHandling,
                            proficiencyBonus = props.proficiencyBonus
                        ), props.translations[ARCANA_TRANSLATION] to
                        SavingThrowsItem(
                            title = props.translations[ARCANA_CONTENT_TRANSLATION] ?: "",
                            label = props.translations[ARCANA_TRANSLATION] ?: "",
                            value = abilities.intelligence,
                            proficient = skills.arcana,
                            proficiencyBonus = props.proficiencyBonus
                        ), props.translations[ATHLETICS_TRANSLATION] to
                        SavingThrowsItem(
                            title = props.translations[ATHLETICS_CONTENT_TRANSLATION] ?: "",
                            label = props.translations[ATHLETICS_TRANSLATION] ?: "",
                            value = abilities.strength,
                            proficient = skills.athletics,
                            proficiencyBonus = props.proficiencyBonus
                        ), props.translations[DECEPTION_TRANSLATION] to
                        SavingThrowsItem(
                            title = props.translations[DECEPTION_CONTENT_TRANSLATION] ?: "",
                            label = props.translations[DECEPTION_TRANSLATION] ?: "",
                            value = abilities.charisma,
                            proficient = skills.deception,
                            proficiencyBonus = props.proficiencyBonus
                        ), props.translations[HISTORY_TRANSLATION] to
                        SavingThrowsItem(
                            title = props.translations[HISTORY_CONTENT_TRANSLATION] ?: "",
                            label = props.translations[HISTORY_TRANSLATION] ?: "",
                            value = abilities.intelligence,
                            proficient = skills.history,
                            proficiencyBonus = props.proficiencyBonus
                        ), props.translations[INSIGHT_TRANSLATION] to
                        SavingThrowsItem(
                            title = props.translations[INSIGHT_CONTENT_TRANSLATION] ?: "",
                            label = props.translations[INSIGHT_TRANSLATION] ?: "",
                            value = abilities.wisdom,
                            proficient = skills.insight,
                            proficiencyBonus = props.proficiencyBonus
                        ), props.translations[INTIMIDATION_TRANSLATION] to
                        SavingThrowsItem(
                            title = props.translations[INTIMIDATION_CONTENT_TRANSLATION] ?: "",
                            label = props.translations[INTIMIDATION_TRANSLATION] ?: "",
                            value = abilities.charisma,
                            proficient = skills.intimidation,
                            proficiencyBonus = props.proficiencyBonus
                        ), props.translations[INVESTIGATION_TRANSLATION] to
                        SavingThrowsItem(
                            title = props.translations[INVESTIGATION_CONTENT_TRANSLATION] ?: "",
                            label = props.translations[INVESTIGATION_TRANSLATION] ?: "",
                            value = abilities.intelligence,
                            proficient = skills.investigation,
                            proficiencyBonus = props.proficiencyBonus
                        ), props.translations[MEDICINE_TRANSLATION] to
                        SavingThrowsItem(
                            title = props.translations[MEDICINE_CONTENT_TRANSLATION] ?: "",
                            label = props.translations[MEDICINE_TRANSLATION] ?: "",
                            value = abilities.wisdom,
                            proficient = skills.medicine,
                            proficiencyBonus = props.proficiencyBonus
                        ), props.translations[NATURE_TRANSLATION] to
                        SavingThrowsItem(
                            title = props.translations[NATURE_CONTENT_TRANSLATION] ?: "",
                            label = props.translations[NATURE_TRANSLATION] ?: "",
                            value = abilities.intelligence,
                            proficient = skills.nature,
                            proficiencyBonus = props.proficiencyBonus
                        ), props.translations[PERCEPTION_TRANSLATION] to
                        SavingThrowsItem(
                            title = props.translations[PERCEPTION_CONTENT_TRANSLATION] ?: "",
                            label = props.translations[PERCEPTION_TRANSLATION] ?: "",
                            value = abilities.wisdom,
                            proficient = skills.perception,
                            proficiencyBonus = props.proficiencyBonus
                        ), props.translations[PERFORMANCE_TRANSLATION] to
                        SavingThrowsItem(
                            title = props.translations[PERFORMANCE_CONTENT_TRANSLATION] ?: "",
                            label = props.translations[PERFORMANCE_TRANSLATION] ?: "",
                            value = abilities.charisma,
                            proficient = skills.performance,
                            proficiencyBonus = props.proficiencyBonus
                        ), props.translations[PERSUASION_TRANSLATION] to
                        SavingThrowsItem(
                            title = props.translations[PERSUASION_CONTENT_TRANSLATION] ?: "",
                            label = props.translations[PERSUASION_TRANSLATION] ?: "",
                            value = abilities.charisma,
                            proficient = skills.persuasion,
                            proficiencyBonus = props.proficiencyBonus
                        ), props.translations[RELIGION_TRANSLATION] to
                        SavingThrowsItem(
                            title = props.translations[RELIGION_CONTENT_TRANSLATION] ?: "",
                            label = props.translations[RELIGION_TRANSLATION] ?: "",
                            value = abilities.intelligence,
                            proficient = skills.religion,
                            proficiencyBonus = props.proficiencyBonus
                        ), props.translations[SLEIGHT_OF_HANDS_TRANSLATION] to
                        SavingThrowsItem(
                            title = props.translations[SLEIGHT_OF_HANDS_CONTENT_TRANSLATION] ?: "",
                            label = props.translations[SLEIGHT_OF_HANDS_TRANSLATION] ?: "",
                            value = abilities.dexterity,
                            proficient = skills.sleightOfHands,
                            proficiencyBonus = props.proficiencyBonus
                        ), props.translations[STEALTH_TRANSLATION] to
                        SavingThrowsItem(
                            title = props.translations[STEALTH_CONTENT_TRANSLATION] ?: "",
                            label = props.translations[STEALTH_TRANSLATION] ?: "",
                            value = abilities.dexterity,
                            proficient = skills.stealth,
                            proficiencyBonus = props.proficiencyBonus
                        ), props.translations[SURVIVAL_TRANSLATION] to
                        SavingThrowsItem(
                            title = props.translations[SURVIVAL_CONTENT_TRANSLATION] ?: "",
                            label = props.translations[SURVIVAL_TRANSLATION] ?: "",
                            value = abilities.wisdom,
                            proficient = skills.survival,
                            proficiencyBonus = props.proficiencyBonus
                        )
            ).sortedBy { it.first }.map { it.second }
        }
        CenteredBold { +(props.translations[ENTER_SKILLS_TRANSLATION] ?: "") }
    }
}

private val SavingThrowsElements = FC<SavingThrowsProps> { props ->
    props.elements.forEach { SavingThrowsGridItem { this.item = it } }
}
