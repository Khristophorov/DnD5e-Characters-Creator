package me.khrys.dnd.charcreator.client.components.dialogs

import me.khrys.dnd.charcreator.client.components.buttons.BackButton
import me.khrys.dnd.charcreator.client.components.buttons.Submit
import me.khrys.dnd.charcreator.client.components.validators.TextValidator
import me.khrys.dnd.charcreator.client.components.validators.ValidatorForm
import me.khrys.dnd.charcreator.client.utils.value
import me.khrys.dnd.charcreator.common.ABILITY_MINIMUM
import me.khrys.dnd.charcreator.common.CLASS_JUSTIFY_BETWEEN
import me.khrys.dnd.charcreator.common.ENTER_HIT_POINTS_CONTENT_TRANSLATION
import me.khrys.dnd.charcreator.common.ENTER_HIT_POINTS_TRANSLATION
import me.khrys.dnd.charcreator.common.NEXT_TRANSLATION
import me.khrys.dnd.charcreator.common.VALIDATION_LOWER_0
import mui.material.Dialog
import mui.material.DialogActions
import mui.material.DialogContent
import mui.material.DialogContentText
import mui.material.DialogTitle
import react.FC
import react.useState
import web.cssom.ClassName
import web.html.InputType
import web.html.number

val HitPointsDialog = FC<CharBasedProps>("HitPointsDialog") { props ->
    console.info("Rendering hit points window")
    val (newHitPoints, setNewHitPoints) = useState(0)
    if (props.open) {
        Dialog {
            this.open = props.open
            DialogTitle {
                +(props.translations[ENTER_HIT_POINTS_TRANSLATION] ?: "")
            }
            DialogContent {
                this.dividers = true
                DialogContentText {
                    +(props.translations[ENTER_HIT_POINTS_CONTENT_TRANSLATION] ?: "")
                }
                ValidatorForm {
                    this.onSubmit = {
                        props.character.hitPoints += newHitPoints
                        props.action()
                    }
                    TextValidator {
                        this.type = InputType.number
                        this.value = newHitPoints.toString()
                        this.validators = arrayOf(VALIDATION_LOWER_0)
                        this.errorMessages = arrayOf(props.translations[ABILITY_MINIMUM] ?: "")
                        this.onChange = { event -> setNewHitPoints(event.value().toInt()) }
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
