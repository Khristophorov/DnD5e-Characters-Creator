package me.khrys.dnd.charcreator.client.components.inputs.texts

import emotion.react.css
import me.khrys.dnd.charcreator.client.components.inputs.tooltips.Tooltip
import mui.material.TooltipProps
import react.FC
import react.dom.html.ReactHTML.span
import web.cssom.WhiteSpace.Companion.preWrap
import web.cssom.px

val TextWithTooltip = FC<TooltipProps>("TextWithTooltip") { props ->
    if (props.title.toString().isBlank()) {
        TextSpan {
            this.children = props.children
        }
    }
    else {
        Tooltip {
            this.title = props.title
            span {
                css {
                    maxWidth = 300.px
                    whiteSpace = preWrap
                    padding = 5.px
                }
                +props.children
            }
        }
    }
}

private val TextSpan = FC<TooltipProps>("TextSpan") { props ->
    span {
        css {
            maxWidth = 300.px
            whiteSpace = preWrap
            padding = 5.px
        }
        +props.children
    }
}
