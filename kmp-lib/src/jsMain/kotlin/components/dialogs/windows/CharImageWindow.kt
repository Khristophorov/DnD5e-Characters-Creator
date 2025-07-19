package me.khrys.dnd.charcreator.client.components.dialogs.windows

import kotlinx.browser.document
import me.khrys.dnd.charcreator.client.components.buttons.BackButton
import me.khrys.dnd.charcreator.client.components.buttons.Button
import me.khrys.dnd.charcreator.client.components.buttons.Submit
import me.khrys.dnd.charcreator.client.components.dialogs.CharBasedProps
import me.khrys.dnd.charcreator.client.components.validators.TextValidator
import me.khrys.dnd.charcreator.client.components.validators.ValidatorForm
import me.khrys.dnd.charcreator.client.components.validators.inputProps
import me.khrys.dnd.charcreator.client.utils.imageFromEvent
import me.khrys.dnd.charcreator.client.utils.value
import me.khrys.dnd.charcreator.common.CLASS_DISABLED
import me.khrys.dnd.charcreator.common.CLASS_JUSTIFY_BETWEEN
import me.khrys.dnd.charcreator.common.ENTER_IMAGE_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_IMAGE_TRANSLATION
import me.khrys.dnd.charcreator.common.FILE_INPUT_ID
import me.khrys.dnd.charcreator.common.NEXT_TRANSLATION
import me.khrys.dnd.charcreator.common.UPLOAD_TRANSLATION
import me.khrys.dnd.charcreator.common.VALIDATION_REQUIRED
import mui.material.Dialog
import mui.material.DialogActions
import mui.material.DialogContent
import mui.material.DialogContentText
import mui.material.DialogTitle
import org.w3c.dom.HTMLElement
import org.w3c.files.FileReader
import react.FC
import react.useState
import web.cssom.ClassName
import web.dom.ElementId
import web.html.InputType
import web.html.file

val CharImageWindow = FC<CharBasedProps> { props ->
    if (props.open) {
        console.info("Rendering image window.")
        Dialog {
            this.open = props.open
            val (charImage, setCharImage) = useState("")

            DialogTitle {
                +(props.translations[ENTER_IMAGE_TRANSLATION] ?: "")
            }
            DialogContent {
                this.dividers = true
                DialogContentText {
                    +(props.translations[ENTER_IMAGE_CONTENT_TRANSLATION] ?: "")
                }
                ValidatorForm {
                    this.onSubmit = {
                        setCharImage("")
                        props.action()
                    }
                    TextValidator {
                        this.id = ElementId(FILE_INPUT_ID)
                        this.type = InputType.file
                        this.value = charImage
                        this.className = ClassName(CLASS_DISABLED)
                        this.inputProps = inputProps(accept = "image/*")
                        this.validators = arrayOf(VALIDATION_REQUIRED)
                        this.onChange = { event ->
                            setCharImage(event.value())
                            imageFromEvent(event) { e ->
                                props.character.image = (e.target as FileReader).result as String
                            }
                        }
                    }
                    Button {
                        +(props.translations[UPLOAD_TRANSLATION] ?: "")
                        this.onClick = { (document.getElementById(FILE_INPUT_ID) as HTMLElement).click() }
                    }
                    DialogActions {
                        this.className = ClassName(CLASS_JUSTIFY_BETWEEN)
                        BackButton {
                            this.onClick = {
                                setCharImage("")
                                props.backAction(it)
                            }
                        }
                        Submit {
                            +(props.translations[NEXT_TRANSLATION] ?: "")
                        }
                    }
                }
            }
        }
    }
}
