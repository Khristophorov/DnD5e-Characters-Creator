package me.khrys.dnd.charcreator.client.components.dialogs.windows

import com.ccfraser.muirwik.components.dialog
import com.ccfraser.muirwik.components.dialogActions
import com.ccfraser.muirwik.components.dialogContent
import com.ccfraser.muirwik.components.dialogTitle
import com.ccfraser.muirwik.components.gridContainer
import com.ccfraser.muirwik.components.gridItem
import com.ccfraser.muirwik.components.maxWidth
import com.ccfraser.muirwik.components.styles.Breakpoint.xl
import com.ccfraser.muirwik.components.table
import com.ccfraser.muirwik.components.tableBody
import com.ccfraser.muirwik.components.tableCell
import com.ccfraser.muirwik.components.tableContainer
import com.ccfraser.muirwik.components.tableHead
import com.ccfraser.muirwik.components.tableRow
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
import me.khrys.dnd.charcreator.client.components.validators.dValidatorForm
import me.khrys.dnd.charcreator.client.computeArmorClass
import me.khrys.dnd.charcreator.client.computePassiveSkill
import me.khrys.dnd.charcreator.client.computeProficiencyBonus
import me.khrys.dnd.charcreator.client.getInitiative
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
    dialog(open = props.open) {
        attrs.fullScreen = true
        attrs.maxWidth = xl
        val character = applyFeatures(props.character, translations)
        title(character, translations, props.setOpen)

        dialogContent(dividers = true) {
            val proficiencyBonus = computeProficiencyBonus(1)
            dValidatorForm(onSubmit = { props.setOpen(false) }) {
                gridContainer {
                    mainParameters(character, translations, proficiencyBonus)
                    additionalAbilities(character, translations)
                    features(character, translations)
                    specificParameters(character, translations)
                }
                dialogActions {
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
    gridItem {
        attrs.className = CLASS_PADDINGS
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
    gridContainer {
        gridItem {
            attrs.className = CLASS_BORDERED
            buildSuperiorDices(translations, superiorityDices)
            dCenteredBold(translations[SUPERIORITY_DICES_TRANSLATION] ?: "")
        }
    }
}

private fun RBuilder.image(image: String) {
    gridContainer {
        gridItem {
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
    gridItem {
        gridContainer {
            gridItem {
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
    gridItem {
        gridContainer {
            gridItem {
                attrs.className = CLASS_PADDINGS
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
    gridItem {
        mainSkills(character, translations, proficiencyBonus)
        passivePerception(translations, character, proficiencyBonus)
        languageAndProficiencies(translations, character)
    }
}

private fun RBuilder.languageAndProficiencies(
    translations: Map<String, String>,
    character: Character
) {
    gridItem {
        attrs.className = "$CLASS_PADDINGS $CLASS_BORDERED"
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
    gridItem {
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
    gridContainer {
        abilities(character.abilities, translations)
        gridItem {
            attrs.className = CLASS_PADDINGS
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
    gridItem {
        attrs.className = CLASS_PADDINGS
        dAbilitiesGrid(abilities, translations)
    }
}

private fun RBuilder.title(
    character: Character,
    translations: Map<String, String>,
    setOpen: (Boolean) -> Unit
) {
    dialogTitle(text = "") {
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
    tableContainer {
        table {
            tableHead {
                tableRow {
                    tableCell {
                        styledP {
                            +(translations[DICE_TRANSLATION] ?: "")
                        }
                    }
                    tableCell {
                        styledP {
                            +(translations[QUANTITY_TRANSLATION] ?: "")
                        }
                    }
                }
            }
            tableBody {
                superiorityDices.forEach { superiorityDice ->
                    tableRow {
                        tableCell {
                            styledP {
                                +superiorityDice.dice.toString().lowercase()
                            }
                        }
                        tableCell {
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
