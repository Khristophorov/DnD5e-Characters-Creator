package me.khrys.dnd.charcreator.client.components.buttons

import me.khrys.dnd.charcreator.client.components.inputs.tooltips.DelayedTooltip
import me.khrys.dnd.charcreator.client.utils.playSound
import me.khrys.dnd.charcreator.common.BUTTON_SOUND_ID
import mui.icons.material.RadioButtonChecked
import mui.icons.material.RadioButtonUnchecked
import mui.material.Checkbox
import mui.material.CheckboxProps
import mui.material.FormControlLabel
import react.FC
import react.Props
import react.ReactNode
import react.createElement

external interface CheckboxWithLabelProps : CheckboxProps {
    var label: ReactNode
}

val CheckboxWithLabel = FC<CheckboxWithLabelProps> { props ->
    val checkBox = FC<Props> {
        Checkbox {
            this.checked = props.checked
            this.icon = createElement(RadioButtonUnchecked)
            this.checkedIcon = createElement(RadioButtonChecked)
            this.onChange = { event, value ->
                playSound(BUTTON_SOUND_ID)
                props.onChange?.let { it(event, value) }
            }
        }
    }
    DelayedTooltip {
        this.title = ReactNode(props.title ?: "")
        FormControlLabel {
            label = props.label
            control = createElement(checkBox)
            checked = props.checked
        }
    }
}
