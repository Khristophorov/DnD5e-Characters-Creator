package me.khrys.dnd.charcreator.client.components.buttons

import me.khrys.dnd.charcreator.client.utils.playSound
import me.khrys.dnd.charcreator.common.BUTTON_SOUND_ID
import mui.material.Button
import mui.material.ButtonColor.Companion.primary
import mui.material.ButtonProps
import mui.material.ButtonVariant.Companion.contained
import mui.material.Size.small
import react.FC
import react.dom.events.MouseEvent
import web.html.HTMLButtonElement

typealias ButtonAction = (MouseEvent<HTMLButtonElement, *>) -> Unit

val Button = FC<ButtonProps>("Button") { props ->
    Button {
        +props.children
        this.color = primary
        this.variant = contained
        this.size = small
        this.onClick = { event ->
            playSound(BUTTON_SOUND_ID)
            props.onClick?.let { it(event) }
        }
        this.type = props.type
    }
}
