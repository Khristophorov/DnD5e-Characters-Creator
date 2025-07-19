package me.khrys.dnd.charcreator.client.components.inputs.texts

import emotion.react.css
import mui.material.InputLabel
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.p
import web.cssom.WhiteSpace.Companion.preWrap
import web.cssom.px

external interface WrappedTextProps : Props {
    var label: String
    var values: Collection<String>
}

val WrappedText = FC<WrappedTextProps>("WrappedText") { props ->
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
