package me.khrys.dnd.charcreator.client.components.buttons

import emotion.react.css
import mui.icons.material.Close
import mui.material.ButtonProps
import react.FC
import react.dom.html.ReactHTML.div
import web.cssom.Float

val CloseButton = FC<ButtonProps>("CloseButton") { props ->
    div {
        css {
            float = Float.right
        }
        IconButton {
            this.onClick = props.onClick
            Close()
        }
    }
}
