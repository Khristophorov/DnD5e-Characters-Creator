package me.khrys.dnd.charcreator.client.components.dialogs

import com.ccfraser.muirwik.components.dialog.mDialog
import com.ccfraser.muirwik.components.dialog.mDialogActions
import com.ccfraser.muirwik.components.dialog.mDialogContent
import com.ccfraser.muirwik.components.dialog.mDialogTitle
import com.ccfraser.muirwik.components.mGridContainer
import com.ccfraser.muirwik.components.mGridItem
import com.ccfraser.muirwik.components.table.mTable
import com.ccfraser.muirwik.components.table.mTableBody
import com.ccfraser.muirwik.components.table.mTableCell
import com.ccfraser.muirwik.components.table.mTableContainer
import com.ccfraser.muirwik.components.table.mTableHead
import com.ccfraser.muirwik.components.table.mTableRow
import kotlinx.css.height
import kotlinx.css.px
import kotlinx.css.width
import kotlinx.html.classes
import me.khrys.dnd.charcreator.client.TranslationsContext
import me.khrys.dnd.charcreator.client.applyFeatures
import me.khrys.dnd.charcreator.client.components.closeButton
import me.khrys.dnd.charcreator.client.components.dAbilityBox
import me.khrys.dnd.charcreator.client.components.dCenteredBold
import me.khrys.dnd.charcreator.client.components.dOneValueInput
import me.khrys.dnd.charcreator.client.components.dSavingThrowsGridItem
import me.khrys.dnd.charcreator.client.components.dSubmit
import me.khrys.dnd.charcreator.client.components.dTextBox
import me.khrys.dnd.charcreator.client.components.dTextWithTooltip
import me.khrys.dnd.charcreator.client.components.dTitledInput
import me.khrys.dnd.charcreator.client.components.dWrappedText
import me.khrys.dnd.charcreator.client.computeArmorClass
import me.khrys.dnd.charcreator.client.computePassiveSkill
import me.khrys.dnd.charcreator.client.computeProficiencyBonus
import me.khrys.dnd.charcreator.client.getInitiative
import me.khrys.dnd.charcreator.client.validators.dValidatorForm
import me.khrys.dnd.charcreator.common.ACROBATICS_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.ACROBATICS_TRANSLATION
import me.khrys.dnd.charcreator.common.ANIMAL_HANDLING_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.ANIMAL_HANDLING_TRANSLATION
import me.khrys.dnd.charcreator.common.ARCANA_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.ARCANA_TRANSLATION
import me.khrys.dnd.charcreator.common.ARMOR_CLASS_TRANSLATION
import me.khrys.dnd.charcreator.common.ATHLETICS_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.ATHLETICS_TRANSLATION
import me.khrys.dnd.charcreator.common.CHARISMA_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.CHARISMA_TRANSLATION
import me.khrys.dnd.charcreator.common.CLASS_BORDERED
import me.khrys.dnd.charcreator.common.CLASS_INLINE
import me.khrys.dnd.charcreator.common.CLASS_JUSTIFY_BETWEEN
import me.khrys.dnd.charcreator.common.CLASS_PADDINGS
import me.khrys.dnd.charcreator.common.CONSTITUTION_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.CONSTITUTION_TRANSLATION
import me.khrys.dnd.charcreator.common.DECEPTION_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.DECEPTION_TRANSLATION
import me.khrys.dnd.charcreator.common.DEXTERITY_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.DEXTERITY_TRANSLATION
import me.khrys.dnd.charcreator.common.DICE_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_RACE_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_SAVING_THROWS_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_SKILLS_TRANSLATION
import me.khrys.dnd.charcreator.common.FEATURES_TRANSLATION
import me.khrys.dnd.charcreator.common.HISTORY_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.HISTORY_TRANSLATION
import me.khrys.dnd.charcreator.common.INITIATIVE_TRANSLATION
import me.khrys.dnd.charcreator.common.INSIGHT_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.INSIGHT_TRANSLATION
import me.khrys.dnd.charcreator.common.INTELLIGENCE_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.INTELLIGENCE_TRANSLATION
import me.khrys.dnd.charcreator.common.INTIMIDATION_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.INTIMIDATION_TRANSLATION
import me.khrys.dnd.charcreator.common.INVESTIGATION_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.INVESTIGATION_TRANSLATION
import me.khrys.dnd.charcreator.common.LANGUAGES_TRANSLATION
import me.khrys.dnd.charcreator.common.MEDICINE_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.MEDICINE_TRANSLATION
import me.khrys.dnd.charcreator.common.NATURE_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.NATURE_TRANSLATION
import me.khrys.dnd.charcreator.common.PASSIVE_PERCEPTION_TRANSLATION
import me.khrys.dnd.charcreator.common.PERCEPTION_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.PERCEPTION_TRANSLATION
import me.khrys.dnd.charcreator.common.PERFORMANCE_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.PERFORMANCE_TRANSLATION
import me.khrys.dnd.charcreator.common.PERSUASION_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.PERSUASION_TRANSLATION
import me.khrys.dnd.charcreator.common.PROFICIENCIES_TRANSLATION
import me.khrys.dnd.charcreator.common.PROFICIENCY_BONUS_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.PROFICIENCY_BONUS_TRANSLATION
import me.khrys.dnd.charcreator.common.QUANTITY_TRANSLATION
import me.khrys.dnd.charcreator.common.RELIGION_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.RELIGION_TRANSLATION
import me.khrys.dnd.charcreator.common.SAVE_TRANSLATION
import me.khrys.dnd.charcreator.common.SLEIGHT_OF_HANDS_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.SLEIGHT_OF_HANDS_TRANSLATION
import me.khrys.dnd.charcreator.common.SPEED_TRANSLATION
import me.khrys.dnd.charcreator.common.STEALTH_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.STEALTH_TRANSLATION
import me.khrys.dnd.charcreator.common.STRENGTH_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.STRENGTH_TRANSLATION
import me.khrys.dnd.charcreator.common.SUPERIORITY_DICES_TRANSLATION
import me.khrys.dnd.charcreator.common.SURVIVAL_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.SURVIVAL_TRANSLATION
import me.khrys.dnd.charcreator.common.WISDOM_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.WISDOM_TRANSLATION
import me.khrys.dnd.charcreator.common.models.Abilities
import me.khrys.dnd.charcreator.common.models.Character
import react.RBuilder
import react.functionalComponent
import react.useContext
import styled.css
import styled.styledDiv
import styled.styledImg
import styled.styledP

val characterWindow = functionalComponent<CharDialogProps> { props ->
    val translations = useContext(TranslationsContext)
    mDialog(open = props.open, fullScreen = true) {
        val character = applyFeatures(props.character, translations)
        mDialogTitle(text = "") {
            styledDiv {
                attrs.classes = setOf(CLASS_INLINE, CLASS_JUSTIFY_BETWEEN)
                +character.name
                dTitledInput(
                    label = translations[ENTER_RACE_TRANSLATION] ?: "",
                    value = character.subrace._id
                )
                closeButton { props.setOpen(false) }
            }
        }
        mDialogContent(dividers = true) {
            val proficiencyBonus = computeProficiencyBonus(1)
            dValidatorForm(onSubmit = { props.setOpen(false) }) {
                mGridContainer {
                    mGridItem {
                        mGridContainer {
                            mGridItem {
                                mGridContainer {
                                    mGridItem(className = CLASS_PADDINGS) {
                                        dAbilitiesGrid(character.abilities, translations)
                                    }
                                    mGridItem(className = CLASS_PADDINGS) {
                                        dOneValueInput(
                                            header = translations[PROFICIENCY_BONUS_TRANSLATION] ?: "",
                                            value = proficiencyBonus,
                                            title = translations[PROFICIENCY_BONUS_CONTENT_TRANSLATION] ?: "",
                                            readOnly = true
                                        )
                                        dSavingThrowsGrid(character, translations, proficiencyBonus)
                                        dSkillsGrid(character, translations, proficiencyBonus)
                                    }
                                }
                            }
                        }
                        mGridItem {
                            dOneValueInput(
                                header = translations[PASSIVE_PERCEPTION_TRANSLATION] ?: "",
                                value = computePassiveSkill(
                                    ability = character.abilities.wisdom,
                                    proficiencyBonus = proficiencyBonus,
                                    proficient = character.skills.perception
                                )
                            )
                        }
                        mGridItem(className = "$CLASS_PADDINGS $CLASS_BORDERED") {
                            dWrappedText(
                                label = translations[LANGUAGES_TRANSLATION] ?: "",
                                text = character.languages
                            )
                            dWrappedText(
                                label = translations[PROFICIENCIES_TRANSLATION] ?: "",
                                text = character.proficiencies
                            )
                        }
                    }
                    mGridItem {
                        mGridContainer {
                            mGridItem(className = CLASS_PADDINGS) {
                                styledDiv {
                                    attrs.classes = setOf(CLASS_INLINE)
                                    dTextBox(
                                        value = character.computeArmorClass(),
                                        label = translations[ARMOR_CLASS_TRANSLATION] ?: ""
                                    )
                                    dTextBox(
                                        value = character.getInitiative(),
                                        label = translations[INITIATIVE_TRANSLATION] ?: ""
                                    )
                                    dTextBox(
                                        value = character.speed,
                                        label = translations[SPEED_TRANSLATION] ?: ""
                                    )
                                }
                            }
                        }
                    }
                    mGridItem {
                        mGridContainer {
                            mGridItem {
                                styledDiv {
                                    attrs.classes = setOf(CLASS_BORDERED)
                                    character.features.forEach { feature ->
                                        dTextWithTooltip(
                                            text = feature.name,
                                            tooltip = feature.description
                                        )
                                    }
                                    dCenteredBold(translations[FEATURES_TRANSLATION] ?: "")
                                }
                            }
                        }
                    }
                    mGridItem(className = CLASS_PADDINGS) {
                        mGridContainer {
                            mGridItem {
                                styledImg(src = character.image) {
                                    css {
                                        width = 128.px
                                        height = 128.px
                                    }
                                }
                            }
                        }
                        if (character.superiorityDices.isNotEmpty()) {
                            mGridContainer {
                                mGridItem(className = CLASS_BORDERED) {
                                    buildSuperiorDices(translations, character)
                                    dCenteredBold(translations[SUPERIORITY_DICES_TRANSLATION] ?: "")
                                }
                            }
                        }
                    }
                }
                mDialogActions {
                    dSubmit(translations[SAVE_TRANSLATION] ?: "")
                }
            }
        }
    }
}

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

private fun RBuilder.buildSuperiorDices(
    translations: Map<String, String>,
    character: Character
) {
    mTableContainer {
        mTable {
            mTableHead {
                mTableRow {
                    mTableCell {
                        styledP {
                            +(translations[DICE_TRANSLATION] ?: "")
                        }
                    }
                    mTableCell {
                        styledP {
                            +(translations[QUANTITY_TRANSLATION] ?: "")
                        }
                    }
                }
            }
            mTableBody {
                character.superiorityDices.forEach { superiorityDice ->
                    mTableRow {
                        mTableCell {
                            styledP {
                                +superiorityDice.dice.toString().lowercase()
                            }
                        }
                        mTableCell {
                            styledP {
                                +superiorityDice.quantity.toString()
                            }
                        }
                    }
                }
            }
        }
    }
}
