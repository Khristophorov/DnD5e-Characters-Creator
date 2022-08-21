package me.khrys.dnd.charcreator.client.components.dialogs.windows

import com.ccfraser.muirwik.components.dialog
import com.ccfraser.muirwik.components.dialogActions
import com.ccfraser.muirwik.components.dialogContent
import com.ccfraser.muirwik.components.dialogContentText
import com.ccfraser.muirwik.components.dialogTitle
import com.ccfraser.muirwik.components.utils.targetValue
import kotlinx.browser.document
import kotlinx.html.InputType.file
import me.khrys.dnd.charcreator.client.components.buttons.dBackButton
import me.khrys.dnd.charcreator.client.components.buttons.dButton
import me.khrys.dnd.charcreator.client.components.buttons.dSubmit
import me.khrys.dnd.charcreator.client.components.validators.InputProps
import me.khrys.dnd.charcreator.client.components.validators.dTextValidator
import me.khrys.dnd.charcreator.client.components.validators.dValidatorForm
import me.khrys.dnd.charcreator.client.imageFromEvent
import me.khrys.dnd.charcreator.common.CLASS_DISABLED
import me.khrys.dnd.charcreator.common.CLASS_JUSTIFY_BETWEEN
import me.khrys.dnd.charcreator.common.ENTER_IMAGE_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_IMAGE_TRANSLATION
import me.khrys.dnd.charcreator.common.FILE_INPUT_ID
import me.khrys.dnd.charcreator.common.NEXT_TRANSLATION
import me.khrys.dnd.charcreator.common.UPLOAD_TRANSLATION
import me.khrys.dnd.charcreator.common.VALIDATION_REQUIRED
import me.khrys.dnd.charcreator.common.models.Character
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import org.w3c.files.FileReader
import react.RBuilder
import react.useState

fun RBuilder.charImageWindow(
    translations: Map<String, String>,
    newCharacter: Character,
    open: Boolean,
    backAction: (Event) -> Unit,
    action: (Event) -> Unit
) {
    dialog(open = open) {
        val (charImage, setCharImage) = useState("")

        dialogTitle(text = translations[ENTER_IMAGE_TRANSLATION] ?: "")
        dialogContent(dividers = true) {
            dialogContentText(text = translations[ENTER_IMAGE_CONTENT_TRANSLATION] ?: "")
            dValidatorForm(onSubmit = { event ->
                setCharImage("")
                action(event)
            }) {
                dTextValidator(
                    id = FILE_INPUT_ID,
                    type = file,
                    value = charImage,
                    className = CLASS_DISABLED,
                    inputProps = InputProps(accept = "image/*"),
                    validators = arrayOf(VALIDATION_REQUIRED),
                    onChange = { event ->
                        setCharImage(event.targetValue as String)
                        imageFromEvent(event) { e -> newCharacter.image = (e.target as FileReader).result as String }
                    }
                )
                dButton(
                    caption = translations[UPLOAD_TRANSLATION] ?: ""
                ) { (document.getElementById(FILE_INPUT_ID) as HTMLElement).click() }
                dialogActions {
                    attrs.className = CLASS_JUSTIFY_BETWEEN
                    dBackButton { event ->
                        setCharImage("")
                        backAction(event)
                    }
                    dSubmit(caption = translations[NEXT_TRANSLATION] ?: "")
                }
            }
        }
    }
}
