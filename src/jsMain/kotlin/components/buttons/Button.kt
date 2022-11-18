package me.khrys.dnd.charcreator.client.components.buttons

import me.khrys.dnd.charcreator.client.playSound
import me.khrys.dnd.charcreator.common.BUTTON_SOUND_ID
import mui.material.Button
import mui.material.ButtonColor.primary
import mui.material.ButtonProps
import mui.material.ButtonVariant.contained
import mui.material.Size.small
import org.w3c.dom.HTMLButtonElement
import react.FC
import react.dom.events.MouseEvent

typealias ButtonAction = (MouseEvent<HTMLButtonElement, *>) -> Unit

val Button = FC<ButtonProps> { props ->
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
