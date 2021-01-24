package me.khrys.dnd.charcreator.client.components

import com.ccfraser.muirwik.components.MColor.primary
import com.ccfraser.muirwik.components.button.MButtonSize.small
import com.ccfraser.muirwik.components.button.MButtonVariant.contained
import com.ccfraser.muirwik.components.button.mButton
import kotlinx.browser.window
import kotlinx.css.Float.right
import kotlinx.css.float
import me.khrys.dnd.charcreator.client.playSound
import me.khrys.dnd.charcreator.common.BUTTON_SOUND_ID
import me.khrys.dnd.charcreator.common.LOGOUT_URL
import org.w3c.dom.events.Event
import react.RBuilder
import styled.css
import styled.styledDiv

fun RBuilder.dButton(caption: String, action: ((Event) -> Unit)) {
    mButton(
        caption = caption,
        color = primary,
        variant = contained,
        onClick = {event ->
            playSound(BUTTON_SOUND_ID)
            action(event)},
        size = small
    )
}

fun RBuilder.logoutButton() {
    styledDiv {
        css { float = right }
        dButton(caption = "Выйти", action = { window.location.href = LOGOUT_URL })
    }
}
