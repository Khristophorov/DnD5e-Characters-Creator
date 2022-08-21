package me.khrys.dnd.charcreator.client.components.inputs.texts

import com.ccfraser.muirwik.components.input
import com.ccfraser.muirwik.components.inputLabel
import react.RBuilder
import styled.styledDiv

fun RBuilder.dTitledInput(
    label: String,
    value: String
) {
    styledDiv {
        inputLabel(label)
        input {
            attrs.readOnly = true
            attrs.value = value
        }
    }
}
