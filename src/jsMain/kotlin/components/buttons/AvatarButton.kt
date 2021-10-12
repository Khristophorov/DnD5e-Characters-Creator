package me.khrys.dnd.charcreator.client.components.buttons

import com.ccfraser.muirwik.components.MTypographyAlign.center
import com.ccfraser.muirwik.components.button.MButtonSize.large
import com.ccfraser.muirwik.components.mAvatar
import com.ccfraser.muirwik.components.mTypography
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
            mAvatar(src = character.image)
        }
        mTypography(
            text = character.name,
            align = center
        )
    }
}
