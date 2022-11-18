package me.khrys.dnd.charcreator.client.components.inputs.texts

import csstype.WhiteSpace.Companion.preWrap
import csstype.px
import emotion.react.css
import me.khrys.dnd.charcreator.client.components.inputs.tooltips.Tooltip
import mui.material.TooltipProps
import react.FC
import react.dom.html.ReactHTML.span

val TextWithTooltip = FC<TooltipProps> { props ->
    Tooltip {
        this.title = props.title
        span {
            css {
                maxWidth = 300.px
                whiteSpace = preWrap
            }
            +props.children
        }
    }
}
