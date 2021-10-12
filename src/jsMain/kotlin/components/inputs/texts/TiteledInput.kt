package me.khrys.dnd.charcreator.client.components.inputs.texts

import com.ccfraser.muirwik.components.input.mInput
import com.ccfraser.muirwik.components.input.mInputLabel
import react.RBuilder
import styled.styledDiv

fun RBuilder.dTitledInput(
    label: String,
    value: String
) {
    styledDiv {
        mInputLabel(label)
        mInput(value) {
            attrs.readOnly = true
        }
    }
}
