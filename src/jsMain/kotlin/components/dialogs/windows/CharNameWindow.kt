package me.khrys.dnd.charcreator.client.components.dialogs.windows

import com.ccfraser.muirwik.components.dialog
import com.ccfraser.muirwik.components.dialogActions
import com.ccfraser.muirwik.components.dialogContent
import com.ccfraser.muirwik.components.dialogContentText
import com.ccfraser.muirwik.components.dialogTitle
import com.ccfraser.muirwik.components.utils.targetValue
import me.khrys.dnd.charcreator.client.components.buttons.dBackButton
import me.khrys.dnd.charcreator.client.components.buttons.dSubmit
import me.khrys.dnd.charcreator.client.components.validators.dTextValidator
import me.khrys.dnd.charcreator.client.components.validators.dValidatorForm
import me.khrys.dnd.charcreator.common.CHAR_NAME_EXISTS
import me.khrys.dnd.charcreator.common.CLASS_JUSTIFY_BETWEEN
import me.khrys.dnd.charcreator.common.ENTER_NAME_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_NAME_LABEL_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_NAME_TRANSLATION
import me.khrys.dnd.charcreator.common.NAME_SHOULD_BE_FILLED_TRANSLATION
import me.khrys.dnd.charcreator.common.NEXT_TRANSLATION
import me.khrys.dnd.charcreator.common.VALIDATION_DUPLICATE_NAME
import me.khrys.dnd.charcreator.common.VALIDATION_REQUIRED
import me.khrys.dnd.charcreator.common.models.Character
import org.w3c.dom.events.Event
import react.RBuilder
import react.useState

fun RBuilder.charNameWindow(
    translations: Map<String, String>,
    newCharacter: Character,
    open: Boolean,
    backAction: (Event) -> Unit,
    action: (Event) -> Unit
) {
    dialog(open = open) {
        val (charName, setCharName) = useState("")

        dialogTitle(text = translations[ENTER_NAME_TRANSLATION] ?: "")
        dialogContent(dividers = true) {
            dialogContentText(text = translations[ENTER_NAME_CONTENT_TRANSLATION] ?: "")
            dValidatorForm(onSubmit = { event ->
                newCharacter.name = charName
                action(event)
            }) {
                dTextValidator(
                    label = translations[ENTER_NAME_LABEL_TRANSLATION] ?: "",
                    value = charName,
                    validators = arrayOf(VALIDATION_REQUIRED, VALIDATION_DUPLICATE_NAME),
                    errorMessages = arrayOf(
                        translations[NAME_SHOULD_BE_FILLED_TRANSLATION] ?: "",
                        translations[CHAR_NAME_EXISTS] ?: ""
                    ),
                    onChange = { event -> setCharName(event.targetValue as String) }
                )
                dialogActions {
                    attrs.className = CLASS_JUSTIFY_BETWEEN
                    dBackButton { event ->
                        backAction(event)
                    }
                    dSubmit(caption = translations[NEXT_TRANSLATION] ?: "")
                }
            }
        }
    }
}
