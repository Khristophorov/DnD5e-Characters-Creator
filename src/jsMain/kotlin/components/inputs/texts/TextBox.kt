package me.khrys.dnd.charcreator.client.components.inputs.texts

import me.khrys.dnd.charcreator.client.components.validators.InputProps
import me.khrys.dnd.charcreator.client.components.validators.TextValidator
import me.khrys.dnd.charcreator.common.CLASS_BORDERED
import me.khrys.dnd.charcreator.common.CLASS_CENTER
import me.khrys.dnd.charcreator.common.VALIDATION_LOWER_0
import me.khrys.dnd.charcreator.common.VALIDATION_UPPER_20
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import web.cssom.ClassName
import web.html.InputType

external interface TextBoxProps : Props {
    var value: String
    var label: String
    var validate: Boolean
    var type: InputType
    var classes: String?
}

val TextBox = FC<TextBoxProps> { props ->
    div {
        className = ClassName("${props.classes} $CLASS_BORDERED $CLASS_CENTER")
        TextValidator {
            this.value = props.value
            this.type = props.type
            this.inputProps = InputProps(readonly = "true")
            this.validators = if (props.validate) arrayOf(VALIDATION_LOWER_0, VALIDATION_UPPER_20) else emptyArray()
        }
        CenteredLabel {
            this.label = props.label
        }
    }
}
