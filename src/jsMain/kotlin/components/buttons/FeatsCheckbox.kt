package me.khrys.dnd.charcreator.client.components.buttons

import me.khrys.dnd.charcreator.common.FEATS_SELECT_TRANSLATION
import react.FC
import react.Props
import react.ReactNode

external interface FeatsProps : Props {
    var checked: Boolean
    var setValue: (Boolean) -> Unit
    var translations: Map<String, String>
}

val FeatsCheckbox = FC<FeatsProps>("FeatsCheckbox") { props ->
    CheckboxWithLabel {
        this.label = ReactNode(props.translations[FEATS_SELECT_TRANSLATION] ?: "")
        this.checked = props.checked
        this.onChange = { _, value -> props.setValue(value) }
    }
}
