package me.khrys.dnd.charcreator.client.components.buttons

import kotlinx.css.Float.right
import kotlinx.css.float
import kotlinx.css.padding
import me.khrys.dnd.charcreator.client.components.dAddIcon
import me.khrys.dnd.charcreator.client.components.inputs.tooltips.dTooltip
import org.w3c.dom.events.Event
import react.RBuilder
import styled.css
import styled.styledDiv

fun RBuilder.dPlusButton(title: String, action: (Event) -> Unit) {
    styledDiv {
        css {
            float = right
            padding = "15px"
        }
        dTooltip(title = title) {
            dFab(action = action) { dAddIcon() }
        }
    }
}
