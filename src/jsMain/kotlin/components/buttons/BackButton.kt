package me.khrys.dnd.charcreator.client.components.buttons

import kotlinx.css.Float.left
import kotlinx.css.float
import me.khrys.dnd.charcreator.client.components.dArrowBackIcon
import org.w3c.dom.events.Event
import react.RBuilder
import styled.css
import styled.styledDiv

fun RBuilder.dBackButton(action: (Event) -> Unit) {
    styledDiv {
        css { float = left }
        dFab(action = action) { dArrowBackIcon() }
    }
}
