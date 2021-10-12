package me.khrys.dnd.charcreator.client.components.inputs.texts

import com.ccfraser.muirwik.components.input.mInputLabel
import kotlinx.css.JustifyContent
import kotlinx.css.justifyContent
import kotlinx.html.classes
import me.khrys.dnd.charcreator.common.CLASS_INLINE
import react.RBuilder
import styled.css
import styled.styledDiv

fun RBuilder.dCenteredLabel(
    label: String
) {
    styledDiv {
        attrs.classes = setOf(CLASS_INLINE)
        css {
            justifyContent = JustifyContent.center
        }
        mInputLabel(caption = label.uppercase(), shrink = true)
    }
}
