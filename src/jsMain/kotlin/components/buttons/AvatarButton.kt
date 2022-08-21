package me.khrys.dnd.charcreator.client.components.buttons

import com.ccfraser.muirwik.components.ButtonSize.large
import com.ccfraser.muirwik.components.TypographyAlign.center
import com.ccfraser.muirwik.components.align
import com.ccfraser.muirwik.components.avatar
import com.ccfraser.muirwik.components.typography
import kotlinx.css.Display.block
import kotlinx.css.display
import kotlinx.css.margin
import me.khrys.dnd.charcreator.common.CLASS_CENTER
import me.khrys.dnd.charcreator.common.models.Character
import org.w3c.dom.events.Event
import react.RBuilder
import styled.css
import styled.styledDiv

fun RBuilder.dAvatarButton(character: Character, action: (Event) -> Unit) {
    styledDiv {
        css {
            display = block
            margin = "5px"
        }
        dFab(size = large, className = CLASS_CENTER, action = action) {
            avatar(src = character.image)
        }
        typography(
            text = character.name
        ) {
            attrs.align = center
        }
    }
}
