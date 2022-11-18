package me.khrys.dnd.charcreator.client.components.buttons

import csstype.Float
import emotion.react.css
import mui.icons.material.ArrowBack
import mui.material.ButtonProps
import react.FC
import react.dom.html.ReactHTML.div

val BackButton = FC<ButtonProps> { props ->
    div {
        css { float = Float.left }
        Fab {
            this.onClick = props.onClick
            ArrowBack()
        }
    }
}
