package me.khrys.dnd.charcreator.client.components.buttons

import com.ccfraser.muirwik.components.MColor.primary
import com.ccfraser.muirwik.components.button.MButtonSize.small
import com.ccfraser.muirwik.components.button.MButtonVariant.contained
import com.ccfraser.muirwik.components.button.mButton
import me.khrys.dnd.charcreator.client.playSound
import me.khrys.dnd.charcreator.common.BUTTON_SOUND_ID
import org.w3c.dom.events.Event
import react.RBuilder

fun RBuilder.dButton(caption: String, action: (Event) -> Unit) {
    mButton(
        caption = caption,
        color = primary,
        variant = contained,
        onClick = { event ->
            playSound(BUTTON_SOUND_ID)
            action(event)
        },
        size = small
    )
}
