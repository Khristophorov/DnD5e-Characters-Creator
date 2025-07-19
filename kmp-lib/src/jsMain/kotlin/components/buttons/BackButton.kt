package me.khrys.dnd.charcreator.client.components.buttons

import emotion.react.css
import mui.icons.material.ArrowBack
import mui.material.ButtonProps
import react.FC
import react.dom.html.ReactHTML.div
import web.cssom.Float

val BackButton = FC<ButtonProps>("BackButton") { props ->
    div {
        css { float = Float.left }
        Fab {
            this.onClick = props.onClick
            ArrowBack()
        }
    }
}
