package me.khrys.dnd.charcreator.client.utils

import kotlinx.browser.document
import org.w3c.dom.Audio
import org.w3c.dom.HTMLDataElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.files.Blob
import org.w3c.files.FileReader
import org.w3c.files.get

fun playSound(id: String) {
    val audio = document.getElementById(id) as Audio
    audio.play()
}

fun imageFromEvent(event: Event, callback: (Event) -> Unit) {
    val file = event.target.unsafeCast<HTMLInputElement>().files?.get(0) ?: Blob()
    val reader = FileReader()
    reader.onloadend = callback
    reader.readAsDataURL(file)
}

fun Event.value() = this.target.unsafeCast<HTMLDataElement>().value
