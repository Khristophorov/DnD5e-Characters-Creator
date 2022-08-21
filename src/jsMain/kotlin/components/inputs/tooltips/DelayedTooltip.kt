package me.khrys.dnd.charcreator.client.components.inputs.tooltips

import com.ccfraser.muirwik.components.TooltipProps
import com.ccfraser.muirwik.components.tooltip
import react.RBuilder
import styled.StyledHandler

const val ENTER_DELAY = 1_000

fun RBuilder.dDelayedTooltip(title: String, handler: StyledHandler<TooltipProps>? = null) {
    tooltip(title = title) {
        attrs.enterDelay = ENTER_DELAY
        handler?.let { handler() }
    }
}
