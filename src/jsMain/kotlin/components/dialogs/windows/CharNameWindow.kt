package me.khrys.dnd.charcreator.client.components.dialogs.windows

import csstype.ClassName
import me.khrys.dnd.charcreator.client.components.buttons.BackButton
import me.khrys.dnd.charcreator.client.components.buttons.Submit
import me.khrys.dnd.charcreator.client.components.dialogs.CharBasedProps
import me.khrys.dnd.charcreator.client.components.validators.TextValidator
import me.khrys.dnd.charcreator.client.components.validators.ValidatorForm
import me.khrys.dnd.charcreator.client.utils.value
import me.khrys.dnd.charcreator.common.CHAR_NAME_EXISTS
import me.khrys.dnd.charcreator.common.CLASS_JUSTIFY_BETWEEN
import me.khrys.dnd.charcreator.common.ENTER_NAME_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_NAME_LABEL_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_NAME_TRANSLATION
import me.khrys.dnd.charcreator.common.NAME_SHOULD_BE_FILLED_TRANSLATION
import me.khrys.dnd.charcreator.common.NEXT_TRANSLATION
import me.khrys.dnd.charcreator.common.VALIDATION_DUPLICATE_NAME
import me.khrys.dnd.charcreator.common.VALIDATION_REQUIRED
import mui.material.Dialog
import mui.material.DialogActions
import mui.material.DialogContent
import mui.material.DialogContentText
import mui.material.DialogTitle
import react.FC
import react.useState

val CharNameWindow = FC<CharBasedProps> { props ->
    if (props.open) {
        Dialog {
            this.open = props.open
            val (charName, setCharName) = useState("")

            DialogTitle {
                +(props.translations[ENTER_NAME_TRANSLATION] ?: "")
            }
            DialogContent {
                this.dividers = true
                DialogContentText {
                    +(props.translations[ENTER_NAME_CONTENT_TRANSLATION] ?: "")
                }
                ValidatorForm {
                    this.onSubmit = {
                        props.character.name = charName
                        props.action()
                    }
                    TextValidator {
                        this.label = props.translations[ENTER_NAME_LABEL_TRANSLATION] ?: ""
                        this.value = charName
                        this.validators = arrayOf(VALIDATION_REQUIRED, VALIDATION_DUPLICATE_NAME)
                        this.errorMessages = arrayOf(
                            props.translations[NAME_SHOULD_BE_FILLED_TRANSLATION] ?: "",
                            props.translations[CHAR_NAME_EXISTS] ?: ""
                        )
                        this.onChange = { event -> setCharName(event.value()) }
                    }
                    DialogActions {
                        this.className = ClassName(CLASS_JUSTIFY_BETWEEN)
                        BackButton {
                            this.onClick = props.backAction
                        }
                        Submit { +(props.translations[NEXT_TRANSLATION] ?: "") }
                    }
                }
            }
        }
    }
}
