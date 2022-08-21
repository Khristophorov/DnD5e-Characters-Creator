package me.khrys.dnd.charcreator.client.components.dialogs.windows

import com.ccfraser.muirwik.components.dialog
import com.ccfraser.muirwik.components.dialogActions
import com.ccfraser.muirwik.components.dialogContent
import com.ccfraser.muirwik.components.dialogContentText
import com.ccfraser.muirwik.components.dialogTitle
import com.ccfraser.muirwik.components.mFormGroup
import kotlinx.html.classes
import me.khrys.dnd.charcreator.client.components.buttons.dBackButton
import me.khrys.dnd.charcreator.client.components.buttons.dCheckboxWithLabel
import me.khrys.dnd.charcreator.client.components.buttons.dSubmit
import me.khrys.dnd.charcreator.client.components.validators.dValidatorForm
import me.khrys.dnd.charcreator.common.CHARISMA_TRANSLATION
import me.khrys.dnd.charcreator.common.CLASS_INLINE
import me.khrys.dnd.charcreator.common.CLASS_JUSTIFY_BETWEEN
import me.khrys.dnd.charcreator.common.CONSTITUTION_TRANSLATION
import me.khrys.dnd.charcreator.common.DEXTERITY_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_SAVING_THROWS_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_SAVING_THROWS_TRANSLATION
import me.khrys.dnd.charcreator.common.FINISH_TRANSLATION
import me.khrys.dnd.charcreator.common.INTELLIGENCE_TRANSLATION
import me.khrys.dnd.charcreator.common.STRENGTH_TRANSLATION
import me.khrys.dnd.charcreator.common.WISDOM_TRANSLATION
import me.khrys.dnd.charcreator.common.models.Character
import me.khrys.dnd.charcreator.common.models.SavingThrows
import org.w3c.dom.events.Event
import react.RBuilder
import react.useState
import styled.styledDiv

fun RBuilder.charSavingThrowsWindow(
    translations: Map<String, String>,
    newCharacter: Character,
    open: Boolean,
    backAction: (Event) -> Unit,
    action: (Event) -> Unit
) {
    val (strength, setStrength) = useState(false)
    val (dexterity, setDexterity) = useState(false)
    val (constitution, setConstitution) = useState(false)
    val (intelligence, setIntelligence) = useState(false)
    val (wisdom, setWisdom) = useState(false)
    val (charisma, setCharisma) = useState(false)
    dialog(open = open) {
        dialogTitle(text = translations[ENTER_SAVING_THROWS_TRANSLATION] ?: "")
        dialogContent(dividers = true) {
            dialogContentText(text = translations[ENTER_SAVING_THROWS_CONTENT_TRANSLATION] ?: "")
            dValidatorForm(
                onSubmit = { event ->
                    newCharacter.savingThrows =
                        SavingThrows(strength, dexterity, constitution, intelligence, wisdom, charisma)
                    action(event)
                }
            ) {
                styledDiv {
                    attrs.classes = setOf(CLASS_JUSTIFY_BETWEEN, CLASS_INLINE)
                    mFormGroup {
                        strength(translations, strength) { setStrength(it) }
                        dexterity(translations, dexterity) { setDexterity(it) }
                    }
                    mFormGroup {
                        constitution(translations, constitution) { setConstitution(it) }
                        intelligence(translations, intelligence) { setIntelligence(it) }
                    }
                    mFormGroup {
                        wisdom(translations, wisdom) { setWisdom(it) }
                        charisma(translations, charisma) { setCharisma(it) }
                    }
                }

                dialogActions {
                    attrs.className = CLASS_JUSTIFY_BETWEEN
                    dBackButton(backAction)
                    dSubmit(caption = translations[FINISH_TRANSLATION] ?: "")
                }
            }
        }
    }
}

private fun RBuilder.charisma(
    translations: Map<String, String>,
    value: Boolean,
    setCharisma: (Boolean) -> Unit
) {
    dCheckboxWithLabel(
        label = translations[CHARISMA_TRANSLATION] ?: "",
        checked = value,
        onChange = { _, checked -> setCharisma(checked) }
    )
}

private fun RBuilder.wisdom(
    translations: Map<String, String>,
    value: Boolean,
    setWisdom: (Boolean) -> Unit
) {
    dCheckboxWithLabel(
        label = translations[WISDOM_TRANSLATION] ?: "",
        checked = value,
        onChange = { _, checked -> setWisdom(checked) }
    )
}

private fun RBuilder.intelligence(
    translations: Map<String, String>,
    value: Boolean,
    setIntelligence: (Boolean) -> Unit
) {
    dCheckboxWithLabel(
        label = translations[INTELLIGENCE_TRANSLATION] ?: "",
        checked = value,
        onChange = { _, checked -> setIntelligence(checked) }
    )
}

private fun RBuilder.constitution(
    translations: Map<String, String>,
    value: Boolean,
    setConstitution: (Boolean) -> Unit
) {
    dCheckboxWithLabel(
        label = translations[CONSTITUTION_TRANSLATION] ?: "",
        checked = value,
        onChange = { _, checked -> setConstitution(checked) }
    )
}

private fun RBuilder.dexterity(
    translations: Map<String, String>,
    value: Boolean,
    setDexterity: (Boolean) -> Unit
) {
    dCheckboxWithLabel(
        label = translations[DEXTERITY_TRANSLATION] ?: "",
        checked = value,
        onChange = { _, checked -> setDexterity(checked) }
    )
}

private fun RBuilder.strength(
    translations: Map<String, String>,
    value: Boolean,
    setStrength: (Boolean) -> Unit
) {
    dCheckboxWithLabel(
        label = translations[STRENGTH_TRANSLATION] ?: "",
        checked = value,
        onChange = { _, checked -> setStrength(checked) }
    )
}
