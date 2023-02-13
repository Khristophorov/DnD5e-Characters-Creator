package me.khrys.dnd.charcreator.client.components.inputs.texts

import csstype.ClassName
import me.khrys.dnd.charcreator.client.components.validators.InputProps
import me.khrys.dnd.charcreator.client.components.validators.TextValidator
import me.khrys.dnd.charcreator.common.CLASS_ABILITY_BOX
import me.khrys.dnd.charcreator.common.CLASS_BORDERED
import me.khrys.dnd.charcreator.common.CLASS_CENTER
import me.khrys.dnd.charcreator.common.VALIDATION_LOWER_0
import me.khrys.dnd.charcreator.common.VALIDATION_UPPER_20
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import web.html.InputType.number

external interface TextBoxProps : Props {
    var value: String
    var label: String
    var validate: Boolean
}

val TextBox = FC<TextBoxProps> { props ->
    div {
        className = ClassName("$CLASS_ABILITY_BOX $CLASS_BORDERED $CLASS_CENTER")
        TextValidator {
            this.value = props.value
            this.type = number
            this.inputProps = InputProps(readOnly = true)
            this.validators = if (props.validate) arrayOf(VALIDATION_LOWER_0, VALIDATION_UPPER_20) else emptyArray()
        }
        CenteredLabel {
            this.label = props.label
        }
    }
}
