package me.khrys.dnd.charcreator.client.components.buttons

import com.ccfraser.muirwik.components.input
import com.ccfraser.muirwik.components.type
import kotlinx.html.InputType.submit
import me.khrys.dnd.charcreator.client.playSound
import me.khrys.dnd.charcreator.common.BUTTON_SOUND_ID
import react.RBuilder

fun RBuilder.dSubmit(caption: String) {
    input {
        attrs.type = submit
        attrs.value = caption
        attrs.onClick = { playSound(BUTTON_SOUND_ID) }
    }
}
