package me.khrys.dnd.charcreator.client.components.inputs.tooltips

import com.ccfraser.muirwik.components.MTooltipProps
import com.ccfraser.muirwik.components.mTooltip
import react.RBuilder
import styled.StyledHandler

fun RBuilder.dDelayedTooltip(title: String, handler: StyledHandler<MTooltipProps>? = null) {
    mTooltip(title = title, enterDelay = 1_000, handler = handler)
}
