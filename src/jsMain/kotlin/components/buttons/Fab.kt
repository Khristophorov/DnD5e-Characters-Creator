package me.khrys.dnd.charcreator.client.components.buttons

import com.ccfraser.muirwik.components.MColor.primary
import com.ccfraser.muirwik.components.button.MButtonSize
import com.ccfraser.muirwik.components.button.MButtonSize.medium
import com.ccfraser.muirwik.components.button.MFabProps
import com.ccfraser.muirwik.components.button.mFab
import me.khrys.dnd.charcreator.client.playSound
import me.khrys.dnd.charcreator.common.BUTTON_SOUND_ID
import org.w3c.dom.events.Event
import react.RBuilder
import styled.StyledHandler

fun RBuilder.dFab(
    size: MButtonSize = medium,
    className: String? = null,
    action: (Event) -> Unit,
    handler: StyledHandler<MFabProps>? = null
) {
    mFab(
        color = primary,
        size = size,
        onClick = { event ->
            playSound(BUTTON_SOUND_ID)
            action(event)
        },
        className = className,
        handler = handler
    )
}
