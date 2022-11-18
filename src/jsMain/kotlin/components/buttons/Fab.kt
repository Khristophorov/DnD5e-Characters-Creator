package me.khrys.dnd.charcreator.client.components.buttons

import me.khrys.dnd.charcreator.client.playSound
import me.khrys.dnd.charcreator.common.BUTTON_SOUND_ID
import mui.material.Fab
import mui.material.FabColor.primary
import mui.material.FabProps
import mui.material.Size.medium
import react.FC

val Fab = FC<FabProps> { props ->
    console.info("Rendering fab")
    Fab {
        this.size = props.size ?: medium
        this.className = props.className
        this.color = primary
        this.onClick = { event ->
            playSound(BUTTON_SOUND_ID)
            props.onClick?.let { it(event) }
        }
        child(props.children)
    }
}
