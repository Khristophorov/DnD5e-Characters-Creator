package me.khrys.dnd.charcreator.client.components.inputs.texts

import kotlinx.html.InputType.number
import kotlinx.html.classes
import me.khrys.dnd.charcreator.client.components.validators.InputProps
import me.khrys.dnd.charcreator.client.components.validators.dTextValidator
import me.khrys.dnd.charcreator.common.CLASS_ABILITY_BOX
import me.khrys.dnd.charcreator.common.CLASS_BORDERED
import me.khrys.dnd.charcreator.common.CLASS_CENTER
import me.khrys.dnd.charcreator.common.VALIDATION_LOWER_0
import me.khrys.dnd.charcreator.common.VALIDATION_UPPER_20
import react.RBuilder
import styled.styledDiv

fun RBuilder.dTextBox(
    value: Int,
    label: String
) {
    styledDiv {
        attrs.classes = setOf(CLASS_ABILITY_BOX, CLASS_BORDERED, CLASS_CENTER)
        dTextValidator(
            value = value.toString(),
            type = number,
            inputProps = InputProps(readOnly = true),
            validators = arrayOf(VALIDATION_LOWER_0, VALIDATION_UPPER_20)
        )
        dCenteredLabel(label)
    }
}
