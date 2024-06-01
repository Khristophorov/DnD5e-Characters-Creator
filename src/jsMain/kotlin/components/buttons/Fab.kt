package me.khrys.dnd.charcreator.client.components.buttons

import me.khrys.dnd.charcreator.client.utils.playSound
import me.khrys.dnd.charcreator.common.BUTTON_SOUND_ID
import mui.material.Fab
import mui.material.FabColor
import mui.material.FabProps
import mui.material.Size.medium
import react.FC

val Fab = FC<FabProps> { props ->
    console.info("Rendering fab")
    Fab {
        this.size = props.size ?: medium
        this.className = props.className
        this.color = "primary".unsafeCast<FabColor>()
        this.onClick = { event ->
            playSound(BUTTON_SOUND_ID)
            props.onClick?.let { it(event) }
        }
        +props.children
    }
}
