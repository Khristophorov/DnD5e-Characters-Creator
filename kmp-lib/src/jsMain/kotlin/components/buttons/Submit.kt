package me.khrys.dnd.charcreator.client.components.buttons

import mui.material.ButtonProps
import react.FC
import web.html.ButtonType
import web.html.submit

val Submit = FC<ButtonProps>("Submit") { props ->
    Button {
        this.type = ButtonType.submit
        +props.children
    }
}
