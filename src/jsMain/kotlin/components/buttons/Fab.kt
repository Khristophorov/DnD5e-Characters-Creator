package me.khrys.dnd.charcreator.client.components.buttons

import com.ccfraser.muirwik.components.ButtonSize
import com.ccfraser.muirwik.components.ButtonSize.medium
import com.ccfraser.muirwik.components.FabColor.primary
import com.ccfraser.muirwik.components.FabProps
import com.ccfraser.muirwik.components.fab
import me.khrys.dnd.charcreator.client.playSound
import me.khrys.dnd.charcreator.common.BUTTON_SOUND_ID
import org.w3c.dom.events.Event
import react.RBuilder
import styled.StyledHandler

fun RBuilder.dFab(
    size: ButtonSize = medium,
    className: String? = null,
    action: (Event) -> Unit,
    handler: StyledHandler<FabProps>? = null
) {
    fab(
        color = primary,
        size = size
    ) {
        attrs.className = className
        attrs.onClick = { event ->
            playSound(BUTTON_SOUND_ID)
            action(event)
        }
        handler?.let { handler() }
    }
}
