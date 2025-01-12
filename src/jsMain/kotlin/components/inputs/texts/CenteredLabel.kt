package me.khrys.dnd.charcreator.client.components.inputs.texts

import emotion.react.css
import mui.material.InputLabel
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import web.cssom.JustifyContent.Companion.center

external interface LabelProps : Props {
    var label: String
}

val CenteredLabel = FC<LabelProps>("CenteredLabel") { props ->
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
