package me.khrys.dnd.charcreator.client

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import me.khrys.dnd.charcreator.common.*
import me.khrys.dnd.charcreator.common.models.Character
import me.khrys.dnd.charcreator.common.models.Feat
import me.khrys.dnd.charcreator.common.models.Race
import me.khrys.dnd.charcreator.common.models.Translation
import org.w3c.dom.Audio
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.fetch.Headers
import org.w3c.fetch.RequestInit
import org.w3c.files.Blob
import org.w3c.files.FileReader
import org.w3c.files.get

fun playSound(id: String) {
    val audio = document.getElementById(id) as Audio
    audio.play()
}

suspend fun fetchTranslations(): Map<String, String> =
    window.fetch(TRANSLATIONS_URL).await().json().await().unsafeCast<Array<Translation>>()
        .associate { it._id to it.value }

suspend fun fetchCharacters(): Array<Character> =
    window.fetch(CHARACTERS_URL).await().json().await().unsafeCast<Array<Character>>()

suspend fun fetchRaces(): Array<Race> =
    window.fetch(RACES_URL).await().json().await().unsafeCast<Array<Race>>()

suspend fun fetchFeats(): Array<Feat> =
    window.fetch(FEATS_URL).await().json().await().unsafeCast<Array<Feat>>()

fun storeCharacter(character: Character) = MainScope().launch {
    val headers = Headers()
    headers.append(CONTENT_TYPE, TYPE_JSON)
    val request = RequestInit(method = METHOD_POST, headers = headers, body = JSON.stringify(character))
    window.fetch(CHARACTERS_URL, request).await().apply { window.location.reload() }
}

fun imageFromEvent(event: Event, callback: (Event) -> Unit) {
    val file = (event.target as HTMLInputElement).files?.get(0) ?: Blob()
    val reader = FileReader()
    reader.onloadend = callback
    reader.readAsDataURL(file)
}
