package me.khrys.dnd.charcreator.client.components.dialogs.grids

import kotlinx.html.classes
import me.khrys.dnd.charcreator.client.components.dialogs.SavingThrowsItem
import me.khrys.dnd.charcreator.client.components.inputs.dCenteredBold
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
import react.RBuilder
import styled.styledDiv

fun RBuilder.dSkillsGrid(
    character: Character,
    translations: Map<String, String>,
    proficiencyBonus: Int
) = styledDiv {
    attrs.classes = setOf(CLASS_BORDERED)
    val abilities = character.abilities
    val skills = character.skills
    buildSavingThrowsElements(
        listOf(
            translations[ACROBATICS_TRANSLATION] to
                    SavingThrowsItem(
                        title = translations[ACROBATICS_CONTENT_TRANSLATION] ?: "",
                        label = translations[ACROBATICS_TRANSLATION] ?: "",
                        value = abilities.dexterity,
                        proficient = skills.acrobatics,
                        proficiencyBonus = proficiencyBonus
                    ), translations[ANIMAL_HANDLING_TRANSLATION] to
                    SavingThrowsItem(
                        title = translations[ANIMAL_HANDLING_CONTENT_TRANSLATION] ?: "",
                        label = translations[ANIMAL_HANDLING_TRANSLATION] ?: "",
                        value = abilities.wisdom,
                        proficient = skills.animalHandling,
                        proficiencyBonus = proficiencyBonus
                    ), translations[ARCANA_TRANSLATION] to
                    SavingThrowsItem(
                        title = translations[ARCANA_CONTENT_TRANSLATION] ?: "",
                        label = translations[ARCANA_TRANSLATION] ?: "",
                        value = abilities.intelligence,
                        proficient = skills.arcana,
                        proficiencyBonus = proficiencyBonus
                    ), translations[ATHLETICS_TRANSLATION] to
                    SavingThrowsItem(
                        title = translations[ATHLETICS_CONTENT_TRANSLATION] ?: "",
                        label = translations[ATHLETICS_TRANSLATION] ?: "",
                        value = abilities.strength,
                        proficient = skills.athletics,
                        proficiencyBonus = proficiencyBonus
                    ), translations[DECEPTION_TRANSLATION] to
                    SavingThrowsItem(
                        title = translations[DECEPTION_CONTENT_TRANSLATION] ?: "",
                        label = translations[DECEPTION_TRANSLATION] ?: "",
                        value = abilities.charisma,
                        proficient = skills.deception,
                        proficiencyBonus = proficiencyBonus
                    ), translations[HISTORY_TRANSLATION] to
                    SavingThrowsItem(
                        title = translations[HISTORY_CONTENT_TRANSLATION] ?: "",
                        label = translations[HISTORY_TRANSLATION] ?: "",
                        value = abilities.intelligence,
                        proficient = skills.history,
                        proficiencyBonus = proficiencyBonus
                    ), translations[INSIGHT_TRANSLATION] to
                    SavingThrowsItem(
                        title = translations[INSIGHT_CONTENT_TRANSLATION] ?: "",
                        label = translations[INSIGHT_TRANSLATION] ?: "",
                        value = abilities.wisdom,
                        proficient = skills.insight,
                        proficiencyBonus = proficiencyBonus
                    ), translations[INTIMIDATION_TRANSLATION] to
                    SavingThrowsItem(
                        title = translations[INTIMIDATION_CONTENT_TRANSLATION] ?: "",
                        label = translations[INTIMIDATION_TRANSLATION] ?: "",
                        value = abilities.charisma,
                        proficient = skills.intimidation,
                        proficiencyBonus = proficiencyBonus
                    ), translations[INVESTIGATION_TRANSLATION] to
                    SavingThrowsItem(
                        title = translations[INVESTIGATION_CONTENT_TRANSLATION] ?: "",
                        label = translations[INVESTIGATION_TRANSLATION] ?: "",
                        value = abilities.intelligence,
                        proficient = skills.investigation,
                        proficiencyBonus = proficiencyBonus
                    ), translations[MEDICINE_TRANSLATION] to
                    SavingThrowsItem(
                        title = translations[MEDICINE_CONTENT_TRANSLATION] ?: "",
                        label = translations[MEDICINE_TRANSLATION] ?: "",
                        value = abilities.wisdom,
                        proficient = skills.medicine,
                        proficiencyBonus = proficiencyBonus
                    ), translations[NATURE_TRANSLATION] to
                    SavingThrowsItem(
                        title = translations[NATURE_CONTENT_TRANSLATION] ?: "",
                        label = translations[NATURE_TRANSLATION] ?: "",
                        value = abilities.intelligence,
                        proficient = skills.nature,
                        proficiencyBonus = proficiencyBonus
                    ), translations[PERCEPTION_TRANSLATION] to
                    SavingThrowsItem(
                        title = translations[PERCEPTION_CONTENT_TRANSLATION] ?: "",
                        label = translations[PERCEPTION_TRANSLATION] ?: "",
                        value = abilities.wisdom,
                        proficient = skills.perception,
                        proficiencyBonus = proficiencyBonus
                    ), translations[PERFORMANCE_TRANSLATION] to
                    SavingThrowsItem(
                        title = translations[PERFORMANCE_CONTENT_TRANSLATION] ?: "",
                        label = translations[PERFORMANCE_TRANSLATION] ?: "",
                        value = abilities.charisma,
                        proficient = skills.performance,
                        proficiencyBonus = proficiencyBonus
                    ), translations[PERSUASION_TRANSLATION] to
                    SavingThrowsItem(
                        title = translations[PERSUASION_CONTENT_TRANSLATION] ?: "",
                        label = translations[PERSUASION_TRANSLATION] ?: "",
                        value = abilities.charisma,
                        proficient = skills.persuasion,
                        proficiencyBonus = proficiencyBonus
                    ), translations[RELIGION_TRANSLATION] to
                    SavingThrowsItem(
                        title = translations[RELIGION_CONTENT_TRANSLATION] ?: "",
                        label = translations[RELIGION_TRANSLATION] ?: "",
                        value = abilities.intelligence,
                        proficient = skills.religion,
                        proficiencyBonus = proficiencyBonus
                    ), translations[SLEIGHT_OF_HANDS_TRANSLATION] to
                    SavingThrowsItem(
                        title = translations[SLEIGHT_OF_HANDS_CONTENT_TRANSLATION] ?: "",
                        label = translations[SLEIGHT_OF_HANDS_TRANSLATION] ?: "",
                        value = abilities.dexterity,
                        proficient = skills.sleightOfHands,
                        proficiencyBonus = proficiencyBonus
                    ), translations[STEALTH_TRANSLATION] to
                    SavingThrowsItem(
                        title = translations[STEALTH_CONTENT_TRANSLATION] ?: "",
                        label = translations[STEALTH_TRANSLATION] ?: "",
                        value = abilities.dexterity,
                        proficient = skills.stealth,
                        proficiencyBonus = proficiencyBonus
                    ), translations[SURVIVAL_TRANSLATION] to
                    SavingThrowsItem(
                        title = translations[SURVIVAL_CONTENT_TRANSLATION] ?: "",
                        label = translations[SURVIVAL_TRANSLATION] ?: "",
                        value = abilities.wisdom,
                        proficient = skills.survival,
                        proficiencyBonus = proficiencyBonus
                    )
        ).sortedBy { it.first }.map { it.second })
    dCenteredBold(translations[ENTER_SKILLS_TRANSLATION] ?: "")
}

private fun RBuilder.buildSavingThrowsElements(elements: List<SavingThrowsItem>) {
    elements.forEach { dSavingThrowsGridItem(it) }
}
