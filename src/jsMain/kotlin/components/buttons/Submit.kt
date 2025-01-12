package me.khrys.dnd.charcreator.client.components.buttons

import mui.material.ButtonProps
import react.FC
import web.html.ButtonType.Companion.submit

val Submit = FC<ButtonProps>("Submit") { props ->
    Button {
        this.type = submit
        +props.children
    }
}
