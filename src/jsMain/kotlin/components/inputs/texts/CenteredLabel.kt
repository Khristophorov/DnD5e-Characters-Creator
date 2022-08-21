package me.khrys.dnd.charcreator.client.components.inputs.texts

import com.ccfraser.muirwik.components.inputLabel
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
        inputLabel(caption = label.uppercase()) {
            attrs.shrink = true
        }
    }
}
