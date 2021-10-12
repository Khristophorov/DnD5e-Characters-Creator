package me.khrys.dnd.charcreator.client.components.buttons

import com.ccfraser.muirwik.components.button.MIconButtonProps
import com.ccfraser.muirwik.components.button.mIconButton
import me.khrys.dnd.charcreator.client.playSound
import me.khrys.dnd.charcreator.common.BUTTON_SOUND_ID
import org.w3c.dom.events.Event
import react.RBuilder
import styled.StyledHandler

fun RBuilder.dIconButton(action: (Event) -> Unit, handler: StyledHandler<MIconButtonProps>? = null) {
    mIconButton(
        onClick = { event ->
            playSound(BUTTON_SOUND_ID)
            action(event)
        },
        handler = handler
    )
}
