package me.khrys.dnd.charcreator.client.components.buttons

import me.khrys.dnd.charcreator.client.utils.playSound
import me.khrys.dnd.charcreator.common.BUTTON_SOUND_ID
import mui.material.IconButton
import mui.material.IconButtonProps
import react.FC

val IconButton = FC<IconButtonProps> { props ->
    IconButton {
        this.onClick = { event ->
            playSound(BUTTON_SOUND_ID)
            props.onClick?.let { it(event) }
        }
        +props.children
    }
}
