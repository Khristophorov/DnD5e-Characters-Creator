package me.khrys.dnd.charcreator.client.components.dialogs.windows

import com.ccfraser.muirwik.components.dialog.mDialog
import com.ccfraser.muirwik.components.dialog.mDialogActions
import com.ccfraser.muirwik.components.dialog.mDialogContent
import com.ccfraser.muirwik.components.dialog.mDialogContentText
import com.ccfraser.muirwik.components.dialog.mDialogTitle
import com.ccfraser.muirwik.components.targetValue
import kotlinx.css.FlexWrap.wrap
import kotlinx.css.flexWrap
import kotlinx.html.classes
import me.khrys.dnd.charcreator.client.components.buttons.dBackButton
import me.khrys.dnd.charcreator.client.components.buttons.dSubmit
import me.khrys.dnd.charcreator.client.components.inputs.dAbilityBox
import me.khrys.dnd.charcreator.client.extentions.DangerousHTML
import me.khrys.dnd.charcreator.client.components.validators.dValidatorForm
import me.khrys.dnd.charcreator.common.CHARISMA_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.CHARISMA_TRANSLATION
import me.khrys.dnd.charcreator.common.CLASS_INLINE
import me.khrys.dnd.charcreator.common.CLASS_JUSTIFY_BETWEEN
import me.khrys.dnd.charcreator.common.CONSTITUTION_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.CONSTITUTION_TRANSLATION
import me.khrys.dnd.charcreator.common.DANGEROUS_HTML
import me.khrys.dnd.charcreator.common.DEXTERITY_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.DEXTERITY_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_ABILITIES_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_ABILITIES_TRANSLATION
import me.khrys.dnd.charcreator.common.INTELLIGENCE_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.INTELLIGENCE_TRANSLATION
import me.khrys.dnd.charcreator.common.NEXT_TRANSLATION
import me.khrys.dnd.charcreator.common.STRENGTH_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.STRENGTH_TRANSLATION
import me.khrys.dnd.charcreator.common.WISDOM_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.WISDOM_TRANSLATION
import me.khrys.dnd.charcreator.common.models.Abilities
import me.khrys.dnd.charcreator.common.models.Character
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.RBuilder
import react.dom.p
import react.useState
import styled.css
import styled.styledDiv

fun RBuilder.charAbilitiesWindow(
    translations: Map<String, String>,
    newCharacter: Character,
    open: Boolean,
    backAction: (Event) -> Unit,
    action: (Event) -> Unit
) {
    mDialog(open = open) {
        val (strength, setStrength) = useState(10)
        val (dexterity, setDexterity) = useState(10)
        val (constitution, setConstitution) = useState(10)
        val (intelligence, setIntelligence) = useState(10)
        val (wisdom, setWisdom) = useState(10)
        val (charisma, setCharisma) = useState(10)

        mDialogTitle(text = translations[ENTER_ABILITIES_TRANSLATION] ?: "")
        mDialogContent(dividers = true) {
            title(translations[ENTER_ABILITIES_CONTENT_TRANSLATION] ?: "")
            dValidatorForm(
                onSubmit = { event ->
                    newCharacter.abilities =
                        Abilities(strength, dexterity, constitution, intelligence, wisdom, charisma)
                    action(event)
                }) {
                styledDiv {
                    attrs.classes = setOf(CLASS_INLINE)
                    css {
                        flexWrap = wrap
                    }
                    strength(translations, strength) { setStrength(it) }
                    dexterity(translations, dexterity) { setDexterity(it) }
                    constitution(translations, constitution) { setConstitution(it) }
                    intelligence(translations, intelligence) { setIntelligence(it) }
                    wisdom(translations, wisdom) { setWisdom(it) }
                    charisma(translations, charisma) { setCharisma(it) }
                }
                mDialogActions(className = CLASS_JUSTIFY_BETWEEN) {
                    dBackButton(backAction)
                    dSubmit(caption = translations[NEXT_TRANSLATION] ?: "")
                }
            }
        }
    }
}

private fun RBuilder.charisma(
    translations: Map<String, String>,
    value: Int,
    setCharisma: (Int) -> Unit
) {
    dAbilityBox(
        title = translations[CHARISMA_CONTENT_TRANSLATION] ?: "",
        label = translations[CHARISMA_TRANSLATION] ?: "",
        value = value,
        translations = translations,
        onChange = { event ->
            (event.targetValue as String).toIntOrNull()?.let { setCharisma(it) }
        }
    )
}

private fun RBuilder.wisdom(
    translations: Map<String, String>,
    value: Int,
    setWisdom: (Int) -> Unit
) {
    dAbilityBox(
        title = translations[WISDOM_CONTENT_TRANSLATION] ?: "",
        label = translations[WISDOM_TRANSLATION] ?: "",
        value = value,
        translations = translations,
        onChange = { event ->
            (event.target as HTMLInputElement).value.toIntOrNull()?.let { setWisdom(it) }
        }
    )
}

private fun RBuilder.intelligence(
    translations: Map<String, String>,
    value: Int,
    setIntelligence: (Int) -> Unit
) {
    dAbilityBox(
        title = translations[INTELLIGENCE_CONTENT_TRANSLATION] ?: "",
        label = translations[INTELLIGENCE_TRANSLATION] ?: "",
        value = value,
        translations = translations,
        onChange = { event ->
            (event.targetValue as String).toIntOrNull()?.let { setIntelligence(it) }
        }
    )
}

private fun RBuilder.constitution(
    translations: Map<String, String>,
    value: Int,
    setConstitution: (Int) -> Unit
) {
    dAbilityBox(
        title = translations[CONSTITUTION_CONTENT_TRANSLATION] ?: "",
        label = translations[CONSTITUTION_TRANSLATION] ?: "",
        value = value,
        translations = translations,
        onChange = { event ->
            (event.targetValue as String).toIntOrNull()?.let { setConstitution(it) }
        }
    )
}

private fun RBuilder.dexterity(
    translations: Map<String, String>,
    value: Int,
    setDexterity: (Int) -> Unit
) {
    dAbilityBox(
        title = translations[DEXTERITY_CONTENT_TRANSLATION] ?: "",
        label = translations[DEXTERITY_TRANSLATION] ?: "",
        value = value,
        translations = translations,
        onChange = { event ->
            (event.targetValue as String).toIntOrNull()?.let { setDexterity(it) }
        }
    )
}

private fun RBuilder.strength(
    translations: Map<String, String>,
    value: Int,
    setStrength: (Int) -> Unit
) {
    dAbilityBox(
        title = translations[STRENGTH_CONTENT_TRANSLATION] ?: "",
        label = translations[STRENGTH_TRANSLATION] ?: "",
        value = value,
        translations = translations,
        onChange = { event ->
            (event.targetValue as String).toIntOrNull()?.let { setStrength(it) }
        }
    )
}

private fun RBuilder.title(value: String) {
    mDialogContentText("") {
        p {
            attrs[DANGEROUS_HTML] =
                DangerousHTML(value)
        }
    }
}
