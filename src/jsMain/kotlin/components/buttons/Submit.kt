package me.khrys.dnd.charcreator.client.components.buttons

import mui.material.ButtonProps
import react.FC
import react.dom.html.ButtonType.submit

val Submit = FC<ButtonProps> { props ->
    Button {
        this.type = submit
        +props.children
    }
}
