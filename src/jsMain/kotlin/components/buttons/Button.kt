package me.khrys.dnd.charcreator.client.components.buttons

import com.ccfraser.muirwik.components.ButtonColor.primary
import com.ccfraser.muirwik.components.ButtonSize.small
import com.ccfraser.muirwik.components.ButtonVariant.contained
import com.ccfraser.muirwik.components.button
import me.khrys.dnd.charcreator.client.playSound
import me.khrys.dnd.charcreator.common.BUTTON_SOUND_ID
import org.w3c.dom.events.Event
import react.RBuilder

fun RBuilder.dButton(caption: String, action: (Event) -> Unit) {
    button(
        caption = caption,
        color = primary,
        variant = contained,
        size = small
    ) {
        attrs.onClick = { event ->
            playSound(BUTTON_SOUND_ID)
            action(event)
        }
    }
}
