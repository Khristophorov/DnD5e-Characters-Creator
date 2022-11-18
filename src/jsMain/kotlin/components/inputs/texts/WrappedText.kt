package me.khrys.dnd.charcreator.client.components.inputs.texts

import csstype.WhiteSpace.Companion.preWrap
import csstype.px
import emotion.react.css
import mui.material.InputLabel
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.p

external interface WrappedTextProps : Props {
    var label: String
    var values: List<String>
}

val WrappedText = FC<WrappedTextProps> { props ->
    div {
        InputLabel {
            +props.label
        }
        p {
            css {
                maxWidth = 300.px
                whiteSpace = preWrap
            }
            +props.values.joinToString(separator = ", ")
        }
    }
}
