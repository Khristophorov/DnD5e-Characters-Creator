package me.khrys.dnd.charcreator.client.components.buttons

import com.ccfraser.muirwik.components.IconButtonProps
import com.ccfraser.muirwik.components.iconButton
import me.khrys.dnd.charcreator.client.playSound
import me.khrys.dnd.charcreator.common.BUTTON_SOUND_ID
import org.w3c.dom.events.Event
import react.RBuilder
import styled.StyledHandler

fun RBuilder.dIconButton(action: (Event) -> Unit, handler: StyledHandler<IconButtonProps>? = null) {
    iconButton {
        attrs.onClick = { event ->
            playSound(BUTTON_SOUND_ID)
            action(event)
        }
        handler?.let { handler() }
    }
}
