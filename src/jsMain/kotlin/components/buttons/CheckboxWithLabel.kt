package me.khrys.dnd.charcreator.client.components.buttons

import com.ccfraser.muirwik.components.form.MFormControlLabelProps
import com.ccfraser.muirwik.components.form.mFormControlLabel
import com.ccfraser.muirwik.components.mCheckbox
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
    handler: StyledHandler<MFormControlLabelProps>? = null
) {
    val checkBox = buildElement {
        mCheckbox(checked = checked, onChange = { event, value ->
            playSound(BUTTON_SOUND_ID)
            onChange?.let { it(event, value) }
        }) {
            attrs.icon = buildElement { dRadioUncheckedIcon() }
            attrs.checkedIcon = buildElement { dRadioCheckedIcon() }
        }
    }
    dDelayedTooltip(title) {
        mFormControlLabel(label = label, control = checkBox, checked = checked, handler = handler)
    }
}
