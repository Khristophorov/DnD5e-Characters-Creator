package me.khrys.dnd.charcreator.client.components.inputs.tooltips

import mui.material.Tooltip
import mui.material.TooltipProps
import react.FC

const val ENTER_DELAY = 1_000

val DelayedTooltip = FC<TooltipProps>("DelayedTooltip") { props ->
    Tooltip {
        this.title = props.title
        this.enterDelay = ENTER_DELAY
        +props.children
    }
}
