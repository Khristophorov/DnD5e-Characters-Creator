package me.khrys.dnd.charcreator.client.components.buttons

import csstype.Float
import emotion.react.css
import mui.icons.material.Close
import mui.material.ButtonProps
import react.FC
import react.dom.html.ReactHTML.div

val CloseButton = FC<ButtonProps> { props ->
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
