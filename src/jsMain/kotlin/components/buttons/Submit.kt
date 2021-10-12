package me.khrys.dnd.charcreator.client.components.buttons

import com.ccfraser.muirwik.components.input.mInput
import kotlinx.html.InputType.submit
import me.khrys.dnd.charcreator.client.playSound
import me.khrys.dnd.charcreator.common.BUTTON_SOUND_ID
import react.RBuilder

fun RBuilder.dSubmit(caption: String) {
    mInput(type = submit, value = caption) {
        attrs.onClick = { playSound(BUTTON_SOUND_ID) }
    }
}
