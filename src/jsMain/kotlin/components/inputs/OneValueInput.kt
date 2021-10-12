package me.khrys.dnd.charcreator.client.components.inputs

import com.ccfraser.muirwik.components.MTypographyAlign.center
import com.ccfraser.muirwik.components.MTypographyColor.textPrimary
import com.ccfraser.muirwik.components.input.mInput
import com.ccfraser.muirwik.components.mAvatar
import com.ccfraser.muirwik.components.mTypography
import kotlinx.html.classes
import me.khrys.dnd.charcreator.client.components.inputs.tooltips.dDelayedTooltip
import me.khrys.dnd.charcreator.common.CLASS_BACKGROUND
import me.khrys.dnd.charcreator.common.CLASS_BOLD
import me.khrys.dnd.charcreator.common.CLASS_BORDERED
import me.khrys.dnd.charcreator.common.CLASS_CENTER
import me.khrys.dnd.charcreator.common.CLASS_INLINE
import me.khrys.dnd.charcreator.common.CLASS_ROUND_BORDERED
import me.khrys.dnd.charcreator.common.CLASS_TEXT_CENTER
import react.RBuilder
import styled.styledDiv

fun RBuilder.dOneValueInput(
    header: String,
    value: Int,
    title: String = "",
    readOnly: Boolean = false,
    className: String = ""
) {
    dDelayedTooltip(title) {
        styledDiv {
            attrs.classes = setOf(CLASS_INLINE, className)
            mAvatar(className = "$CLASS_CENTER $CLASS_ROUND_BORDERED $CLASS_BACKGROUND") {
                mTypography(text = value.toString(), align = center, color = textPrimary)
            }
            mInput(
                value = header.uppercase(),
                readOnly = readOnly,
                fullWidth = true,
                className = "$CLASS_BORDERED $CLASS_BOLD $CLASS_TEXT_CENTER"
            )
        }
    }
}
