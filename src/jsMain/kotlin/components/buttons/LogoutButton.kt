package me.khrys.dnd.charcreator.client.components.buttons

import kotlinx.browser.window
import kotlinx.css.Float.right
import kotlinx.css.float
import me.khrys.dnd.charcreator.common.LOGOUT_URL
import react.RBuilder
import styled.css
import styled.styledDiv

fun RBuilder.dLogoutButton(caption: String?) {
    styledDiv {
        css { float = right }
        dButton(caption = caption ?: "", action = { window.location.href = LOGOUT_URL })
    }
}
