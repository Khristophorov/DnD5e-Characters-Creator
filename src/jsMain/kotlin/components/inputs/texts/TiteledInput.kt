package me.khrys.dnd.charcreator.client.components.inputs.texts

import mui.material.Input
import mui.material.InputLabel
import react.FC
import react.dom.html.ReactHTML.div

val TitledInput = FC<TextBoxProps> { props ->
    div {
        InputLabel {
            +props.label
        }
        Input {
            this.readOnly = true
            this.value = props.value
        }
    }
}
