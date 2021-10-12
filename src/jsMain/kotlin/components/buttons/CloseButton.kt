package me.khrys.dnd.charcreator.client.components.buttons

import kotlinx.css.Float.right
import kotlinx.css.float
import me.khrys.dnd.charcreator.client.components.dCloseIcon
import org.w3c.dom.events.Event
import react.RBuilder
import styled.css
import styled.styledDiv

fun RBuilder.dCloseButton(action: (Event) -> Unit) {
    styledDiv {
        css {
            float = right
        }
        dIconButton(action = action) { dCloseIcon() }
    }
}
