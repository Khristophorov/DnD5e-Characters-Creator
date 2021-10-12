package me.khrys.dnd.charcreator.client.components.inputs.texts

import kotlinx.css.WhiteSpace.preWrap
import kotlinx.css.maxWidth
import kotlinx.css.px
import kotlinx.css.whiteSpace
import me.khrys.dnd.charcreator.client.components.inputs.tooltips.dTooltip
import react.RBuilder
import styled.css
import styled.styledP

fun RBuilder.dTextWithTooltip(
    text: String,
    tooltip: String
) {
    dTooltip(tooltip) {
        styledP {
            css {
                maxWidth = 300.px
                whiteSpace = preWrap
            }
            +text
        }
    }
}
