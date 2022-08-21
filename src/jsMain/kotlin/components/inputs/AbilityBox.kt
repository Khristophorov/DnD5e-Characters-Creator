package me.khrys.dnd.charcreator.client.components.inputs

import com.ccfraser.muirwik.components.TypographyAlign.center
import com.ccfraser.muirwik.components.TypographyColor.textPrimary
import com.ccfraser.muirwik.components.align
import com.ccfraser.muirwik.components.avatar
import com.ccfraser.muirwik.components.typography
import kotlinx.html.InputType.number
import kotlinx.html.classes
import me.khrys.dnd.charcreator.client.components.inputs.texts.dCenteredLabel
import me.khrys.dnd.charcreator.client.components.inputs.tooltips.dDelayedTooltip
import me.khrys.dnd.charcreator.client.components.validators.InputProps
import me.khrys.dnd.charcreator.client.components.validators.dTextValidator
import me.khrys.dnd.charcreator.client.computeModifier
import me.khrys.dnd.charcreator.common.ABILITY_MAXIMUM
import me.khrys.dnd.charcreator.common.ABILITY_MINIMUM
import me.khrys.dnd.charcreator.common.CLASS_ABILITY_BOX
import me.khrys.dnd.charcreator.common.CLASS_BACKGROUND
import me.khrys.dnd.charcreator.common.CLASS_BORDERED
import me.khrys.dnd.charcreator.common.CLASS_CENTER
import me.khrys.dnd.charcreator.common.CLASS_ROUND_BORDERED
import me.khrys.dnd.charcreator.common.VALIDATION_LOWER_0
import me.khrys.dnd.charcreator.common.VALIDATION_UPPER_20
import org.w3c.dom.events.InputEvent
import react.RBuilder
import styled.styledDiv

fun RBuilder.dAbilityBox(
    title: String,
    label: String,
    value: Int,
    translations: Map<String, String>,
    readOnly: Boolean = false,
    onChange: (InputEvent) -> Unit = {}
) {
    dDelayedTooltip(title) {
        styledDiv {
            attrs.classes = setOf(CLASS_ABILITY_BOX, CLASS_BORDERED, CLASS_CENTER)
            dCenteredLabel(label)
            dTextValidator(
                value = value.toString(),
                type = number,
                inputProps = InputProps(readOnly = readOnly),
                validators = arrayOf(VALIDATION_LOWER_0, VALIDATION_UPPER_20),
                errorMessages = if (readOnly) emptyArray() else arrayOf(
                    translations[ABILITY_MINIMUM] ?: "",
                    translations[ABILITY_MAXIMUM] ?: ""
                ),
                onChange = onChange
            )
            avatar {
                attrs.className = "$CLASS_CENTER $CLASS_ROUND_BORDERED $CLASS_BACKGROUND"
                typography(text = computeModifier(value).toString(), color = textPrimary) {
                    attrs.align = center
                }
            }
        }
    }
}
