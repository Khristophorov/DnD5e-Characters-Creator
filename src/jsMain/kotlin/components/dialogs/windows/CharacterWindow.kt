package me.khrys.dnd.charcreator.client.components.dialogs.windows

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
import me.khrys.dnd.charcreator.client.components.buttons.dCloseButton
import me.khrys.dnd.charcreator.client.components.buttons.dSubmit
import me.khrys.dnd.charcreator.client.components.dialogs.CharDialogProps
import me.khrys.dnd.charcreator.client.components.dialogs.grids.dAbilitiesGrid
import me.khrys.dnd.charcreator.client.components.dialogs.grids.dSavingThrowsGrid
import me.khrys.dnd.charcreator.client.components.dialogs.grids.dSkillsGrid
import me.khrys.dnd.charcreator.client.components.inputs.dCenteredBold
import me.khrys.dnd.charcreator.client.components.inputs.dOneValueInput
import me.khrys.dnd.charcreator.client.components.inputs.texts.dTextBox
import me.khrys.dnd.charcreator.client.components.inputs.texts.dTextWithTooltip
import me.khrys.dnd.charcreator.client.components.inputs.texts.dTitledInput
import me.khrys.dnd.charcreator.client.components.inputs.texts.dWrappedText
import me.khrys.dnd.charcreator.client.computeArmorClass
import me.khrys.dnd.charcreator.client.computePassiveSkill
import me.khrys.dnd.charcreator.client.computeProficiencyBonus
import me.khrys.dnd.charcreator.client.getInitiative
import me.khrys.dnd.charcreator.client.components.validators.dValidatorForm
import me.khrys.dnd.charcreator.common.ARMOR_CLASS_TRANSLATION
import me.khrys.dnd.charcreator.common.CLASS_BORDERED
import me.khrys.dnd.charcreator.common.CLASS_INLINE
import me.khrys.dnd.charcreator.common.CLASS_JUSTIFY_BETWEEN
import me.khrys.dnd.charcreator.common.CLASS_PADDINGS
import me.khrys.dnd.charcreator.common.DICE_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_RACE_TRANSLATION
import me.khrys.dnd.charcreator.common.FEATURES_TRANSLATION
import me.khrys.dnd.charcreator.common.INITIATIVE_TRANSLATION
import me.khrys.dnd.charcreator.common.LANGUAGES_TRANSLATION
import me.khrys.dnd.charcreator.common.PASSIVE_PERCEPTION_TRANSLATION
import me.khrys.dnd.charcreator.common.PROFICIENCIES_TRANSLATION
import me.khrys.dnd.charcreator.common.PROFICIENCY_BONUS_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.PROFICIENCY_BONUS_TRANSLATION
import me.khrys.dnd.charcreator.common.QUANTITY_TRANSLATION
import me.khrys.dnd.charcreator.common.SAVE_TRANSLATION
import me.khrys.dnd.charcreator.common.SPEED_TRANSLATION
import me.khrys.dnd.charcreator.common.SUPERIORITY_DICES_TRANSLATION
import me.khrys.dnd.charcreator.common.models.Abilities
import me.khrys.dnd.charcreator.common.models.Character
import me.khrys.dnd.charcreator.common.models.SuperiorityDice
import react.RBuilder
import react.fc
import react.useContext
import styled.css
import styled.styledDiv
import styled.styledImg
import styled.styledP

val characterWindow = fc<CharDialogProps> { props ->
    val translations = useContext(TranslationsContext)
    mDialog(open = props.open, fullScreen = true) {
        val character = applyFeatures(props.character, translations)
        title(character, translations, props.setOpen)

        mDialogContent(dividers = true) {
            val proficiencyBonus = computeProficiencyBonus(1)
            dValidatorForm(onSubmit = { props.setOpen(false) }) {
                mGridContainer {
                    mainParameters(character, translations, proficiencyBonus)
                    additionalAbilities(character, translations)
                    features(character, translations)
                    specificParameters(character, translations)
                }
                mDialogActions {
                    dSubmit(translations[SAVE_TRANSLATION] ?: "")
                }
            }
        }
    }
}

private fun RBuilder.specificParameters(
    character: Character,
    translations: Map<String, String>
) {
    mGridItem(className = CLASS_PADDINGS) {
        image(character.image)
        if (character.superiorityDices.isNotEmpty()) {
            superiorityDices(translations, character.superiorityDices)
        }
    }
}

private fun RBuilder.superiorityDices(
    translations: Map<String, String>,
    superiorityDices: List<SuperiorityDice>
) {
    mGridContainer {
        mGridItem(className = CLASS_BORDERED) {
            buildSuperiorDices(translations, superiorityDices)
            dCenteredBold(translations[SUPERIORITY_DICES_TRANSLATION] ?: "")
        }
    }
}

private fun RBuilder.image(image: String) {
    mGridContainer {
        mGridItem {
            styledImg(src = image) {
                css {
                    width = 128.px
                    height = 128.px
                }
            }
        }
    }
}

private fun RBuilder.features(
    character: Character,
    translations: Map<String, String>
) {
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
}

private fun RBuilder.additionalAbilities(
    character: Character,
    translations: Map<String, String>
) {
    mGridItem {
        mGridContainer {
            mGridItem(className = CLASS_PADDINGS) {
                styledDiv {
                    attrs.classes = setOf(CLASS_INLINE)
                    armorClass(character.computeArmorClass(), translations)
                    initiative(character.getInitiative(), translations)
                    speed(character.speed, translations)
                }
            }
        }
    }
}

private fun RBuilder.speed(value: Int, translations: Map<String, String>) {
    dTextBox(
        value = value,
        label = translations[SPEED_TRANSLATION] ?: ""
    )
}

private fun RBuilder.initiative(value: Int, translations: Map<String, String>) {
    dTextBox(
        value = value,
        label = translations[INITIATIVE_TRANSLATION] ?: ""
    )
}

private fun RBuilder.armorClass(value: Int, translations: Map<String, String>) {
    dTextBox(
        value = value,
        label = translations[ARMOR_CLASS_TRANSLATION] ?: ""
    )
}

private fun RBuilder.mainParameters(
    character: Character,
    translations: Map<String, String>,
    proficiencyBonus: Int
) {
    mGridItem {
        mainSkills(character, translations, proficiencyBonus)
        passivePerception(translations, character, proficiencyBonus)
        languageAndProficiencies(translations, character)
    }
}

private fun RBuilder.languageAndProficiencies(
    translations: Map<String, String>,
    character: Character
) {
    mGridItem(className = "$CLASS_PADDINGS $CLASS_BORDERED") {
        languages(translations, character.languages)
        proficiencies(translations, character.proficiencies)
    }
}

private fun RBuilder.proficiencies(
    translations: Map<String, String>,
    values: List<String>
) {
    dWrappedText(
        label = translations[PROFICIENCIES_TRANSLATION] ?: "",
        text = values
    )
}

private fun RBuilder.languages(
    translations: Map<String, String>,
    values: List<String>
) {
    dWrappedText(
        label = translations[LANGUAGES_TRANSLATION] ?: "",
        text = values
    )
}

private fun RBuilder.passivePerception(
    translations: Map<String, String>,
    character: Character,
    proficiencyBonus: Int
) {
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
}

private fun RBuilder.mainSkills(
    character: Character,
    translations: Map<String, String>,
    proficiencyBonus: Int
) {
    mGridContainer {
        abilities(character.abilities, translations)
        mGridItem(className = CLASS_PADDINGS) {
            proficiencyBonus(translations, proficiencyBonus)
            dSavingThrowsGrid(character, translations, proficiencyBonus)
            dSkillsGrid(character, translations, proficiencyBonus)
        }
    }
}

private fun RBuilder.proficiencyBonus(
    translations: Map<String, String>,
    proficiencyBonus: Int
) {
    dOneValueInput(
        header = translations[PROFICIENCY_BONUS_TRANSLATION] ?: "",
        value = proficiencyBonus,
        title = translations[PROFICIENCY_BONUS_CONTENT_TRANSLATION] ?: "",
        readOnly = true
    )
}

private fun RBuilder.abilities(
    abilities: Abilities,
    translations: Map<String, String>
) {
    mGridItem(className = CLASS_PADDINGS) {
        dAbilitiesGrid(abilities, translations)
    }
}

private fun RBuilder.title(
    character: Character,
    translations: Map<String, String>,
    setOpen: (Boolean) -> Unit
) {
    mDialogTitle(text = "") {
        styledDiv {
            attrs.classes = setOf(CLASS_INLINE, CLASS_JUSTIFY_BETWEEN)
            +character.name
            dTitledInput(
                label = translations[ENTER_RACE_TRANSLATION] ?: "",
                value = character.subrace._id
            )
            dCloseButton { setOpen(false) }
        }
    }
}

private fun RBuilder.buildSuperiorDices(
    translations: Map<String, String>,
    superiorityDices: List<SuperiorityDice>
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
                superiorityDices.forEach { superiorityDice ->
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
