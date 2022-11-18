package me.khrys.dnd.charcreator.client.components.inputs.texts

import csstype.JustifyContent.Companion.center
import emotion.react.css
import mui.material.InputLabel
import react.FC
import react.Props
import react.dom.html.ReactHTML.div

external interface LabelProps : Props {
    var label: String
}

val CenteredLabel = FC<LabelProps> { props ->
    div {
        css {
            justifyContent = center
        }
        InputLabel {
            +props.label.uppercase()
            this.shrink = true
        }
    }
}
