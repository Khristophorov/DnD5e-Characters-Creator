package me.khrys.dnd.charcreator.client

import kotlinx.browser.document
import org.w3c.dom.Audio

fun playSound(id: String) {
    val audio: Audio = document.getElementById(id) as Audio
    audio.play()
}
