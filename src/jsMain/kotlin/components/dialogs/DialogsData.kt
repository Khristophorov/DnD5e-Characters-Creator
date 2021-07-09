package me.khrys.dnd.charcreator.client.components.dialogs

import com.ccfraser.muirwik.components.form.MFormControlLabelProps
import org.w3c.dom.events.Event
import styled.StyledHandler

data class CheckboxWithLabelModel(
    val title: String,
    val label: String,
    val checked: Boolean,
    val onChange: (Event, Boolean) -> Unit,
    val handler: StyledHandler<MFormControlLabelProps>? = null
)

data class SavingThrowsItem(
    val title: String = "",
    val label: String,
    val value: Int,
    val proficient: Boolean,
    val proficiencyBonus: Int
)
