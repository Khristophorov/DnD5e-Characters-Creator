package me.khrys.dnd.charcreator.client.components.buttons

import com.ccfraser.muirwik.components.FormControlLabelProps
import com.ccfraser.muirwik.components.checkbox
import com.ccfraser.muirwik.components.formControlLabel
import me.khrys.dnd.charcreator.client.components.dRadioCheckedIcon
import me.khrys.dnd.charcreator.client.components.dRadioUncheckedIcon
import me.khrys.dnd.charcreator.client.components.inputs.tooltips.dDelayedTooltip
import me.khrys.dnd.charcreator.client.playSound
import me.khrys.dnd.charcreator.common.BUTTON_SOUND_ID
import org.w3c.dom.events.Event
import react.RBuilder
import react.buildElement
import styled.StyledHandler

fun RBuilder.dCheckboxWithLabel(
    title: String = "",
    label: String,
    checked: Boolean,
    onChange: ((Event, Boolean) -> Unit)? = null,
    handler: StyledHandler<FormControlLabelProps>? = null
) {
    val checkBox = buildElement {
        checkbox(checked = checked) {
            attrs.icon = buildElement { dRadioUncheckedIcon() }
            attrs.checkedIcon = buildElement { dRadioCheckedIcon() }
            attrs.onChange = { event, value ->
                playSound(BUTTON_SOUND_ID)
                onChange?.let { it(event, value) }
            }
        }
    }
    dDelayedTooltip(title) {
        formControlLabel(label = label, control = checkBox) {
            attrs.checked = checked
            handler?.let { handler() }
        }
    }
}
