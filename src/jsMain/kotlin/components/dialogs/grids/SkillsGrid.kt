package me.khrys.dnd.charcreator.client.components.dialogs.grids

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
import me.khrys.dnd.charcreator.common.CHARISMA_TRANSLATION
import me.khrys.dnd.charcreator.common.CLASS_BORDERED
import me.khrys.dnd.charcreator.common.CONSTITUTION_TRANSLATION
import me.khrys.dnd.charcreator.common.DECEPTION_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.DECEPTION_TRANSLATION
import me.khrys.dnd.charcreator.common.DEXTERITY_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_SKILLS_TRANSLATION
import me.khrys.dnd.charcreator.common.HISTORY_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.HISTORY_TRANSLATION
import me.khrys.dnd.charcreator.common.INSIGHT_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.INSIGHT_TRANSLATION
import me.khrys.dnd.charcreator.common.INTELLIGENCE_TRANSLATION
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
import me.khrys.dnd.charcreator.common.STRENGTH_TRANSLATION
import me.khrys.dnd.charcreator.common.SURVIVAL_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.SURVIVAL_TRANSLATION
import me.khrys.dnd.charcreator.common.WISDOM_TRANSLATION
import me.khrys.dnd.charcreator.common.models.Abilities
import me.khrys.dnd.charcreator.common.models.Character
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import web.cssom.ClassName

external interface SkillsProps : Props {
    var character: Character
    var translations: Map<String, String>
    var proficiencyBonus: Int
}

private external interface SavingThrowsProps : Props {
    var elements: List<SavingThrowsItem>
}

val SkillsGrid = FC<SkillsProps>("SkillsGrid") { props ->
    div {
        this.className = ClassName(CLASS_BORDERED)
        val skills = props.character.skills.associateBy { it.name }
        val translations = props.translations
        val abilities = translatedAbilities(props.character.abilities, translations)
        SavingThrowsElements {
            this.elements = skillsDescriptions(translations).entries.sortedBy { it.key }
                .map { it.toPair() }
                .map { (name, content) ->
                    val skill = skills[name] ?: throw IllegalArgumentException("No skill$name")
                    SavingThrowsItem(
                        title = content ?: "",
                        label = name ?: "",
                        value = abilities[skill.ability]?.value ?: 0,
                        proficient = skill.proficient,
                        proficiencyBonus = props.proficiencyBonus,
                        additionalBonus = skill.additionalBonus
                    )
                }
        }
        CenteredBold { +(translations[ENTER_SKILLS_TRANSLATION] ?: "") }
    }
}

private val SavingThrowsElements = FC<SavingThrowsProps> { props ->
    props.elements.forEach { SavingThrowsGridItem { this.item = it } }
}

private fun skillsDescriptions(translations: Map<String, String>) = mapOf(
    translations[ACROBATICS_TRANSLATION] to translations[ACROBATICS_CONTENT_TRANSLATION],
    translations[ANIMAL_HANDLING_TRANSLATION] to translations[ANIMAL_HANDLING_CONTENT_TRANSLATION],
    translations[ARCANA_TRANSLATION] to translations[ARCANA_CONTENT_TRANSLATION],
    translations[ATHLETICS_TRANSLATION] to translations[ATHLETICS_CONTENT_TRANSLATION],
    translations[DECEPTION_TRANSLATION] to translations[DECEPTION_CONTENT_TRANSLATION],
    translations[HISTORY_TRANSLATION] to translations[HISTORY_CONTENT_TRANSLATION],
    translations[INSIGHT_TRANSLATION] to translations[INSIGHT_CONTENT_TRANSLATION],
    translations[INTIMIDATION_TRANSLATION] to translations[INTIMIDATION_CONTENT_TRANSLATION],
    translations[INVESTIGATION_TRANSLATION] to translations[INVESTIGATION_CONTENT_TRANSLATION],
    translations[MEDICINE_TRANSLATION] to translations[MEDICINE_CONTENT_TRANSLATION],
    translations[NATURE_TRANSLATION] to translations[NATURE_CONTENT_TRANSLATION],
    translations[PERCEPTION_TRANSLATION] to translations[PERCEPTION_CONTENT_TRANSLATION],
    translations[PERFORMANCE_TRANSLATION] to translations[PERFORMANCE_CONTENT_TRANSLATION],
    translations[PERSUASION_TRANSLATION] to translations[PERSUASION_CONTENT_TRANSLATION],
    translations[RELIGION_TRANSLATION] to translations[RELIGION_CONTENT_TRANSLATION],
    translations[SLEIGHT_OF_HANDS_TRANSLATION] to translations[SLEIGHT_OF_HANDS_CONTENT_TRANSLATION],
    translations[STEALTH_TRANSLATION] to translations[STEALTH_CONTENT_TRANSLATION],
    translations[SURVIVAL_TRANSLATION] to translations[SURVIVAL_CONTENT_TRANSLATION]
)

fun translatedAbilities(abilities: Abilities, translations: Map<String, String>) = mapOf(
    translations[STRENGTH_TRANSLATION] to abilities.strength,
    translations[DEXTERITY_TRANSLATION] to abilities.dexterity,
    translations[CONSTITUTION_TRANSLATION] to abilities.constitution,
    translations[INTELLIGENCE_TRANSLATION] to abilities.intelligence,
    translations[WISDOM_TRANSLATION] to abilities.wisdom,
    translations[CHARISMA_TRANSLATION] to abilities.charisma
)
