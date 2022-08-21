package me.khrys.dnd.charcreator.client.components.inputs

import com.ccfraser.muirwik.components.TypographyAlign.center
import com.ccfraser.muirwik.components.TypographyColor.textPrimary
import com.ccfraser.muirwik.components.align
import com.ccfraser.muirwik.components.avatar
import com.ccfraser.muirwik.components.input
import com.ccfraser.muirwik.components.typography
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
            avatar {
                attrs.className = "$CLASS_CENTER $CLASS_ROUND_BORDERED $CLASS_BACKGROUND"
                typography(text = value.toString(), color = textPrimary) {
                    attrs.align = center
                }
            }
            input {
                attrs.value = header.uppercase()
                attrs.readOnly = readOnly
                attrs.fullWidth = true
                attrs.className = "$CLASS_BORDERED $CLASS_BOLD $CLASS_TEXT_CENTER"
            }
        }
    }
}
