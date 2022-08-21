package me.khrys.dnd.charcreator.client

import com.ccfraser.muirwik.components.circularProgress
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.khrys.dnd.charcreator.common.CHARACTERS_URL
import me.khrys.dnd.charcreator.common.CONTENT_TYPE
import me.khrys.dnd.charcreator.common.FEATS_URL
import me.khrys.dnd.charcreator.common.METHOD_POST
import me.khrys.dnd.charcreator.common.RACES_URL
import me.khrys.dnd.charcreator.common.TRANSLATIONS_URL
import me.khrys.dnd.charcreator.common.TYPE_JSON
import me.khrys.dnd.charcreator.common.models.Character
import me.khrys.dnd.charcreator.common.models.DnDFunction
import me.khrys.dnd.charcreator.common.models.Feat
import me.khrys.dnd.charcreator.common.models.Feature
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
import react.RBuilder
import react.StateSetter
import react.useState

fun playSound(id: String) {
    val audio = document.getElementById(id) as Audio
    audio.play()
}

suspend fun fetchTranslations(): Map<String, String> =
    fetchModel<List<Translation>>(TRANSLATIONS_URL).associate { it._id to it.value }

suspend fun fetchCharacters(): List<Character> = fetchModel(CHARACTERS_URL)

suspend fun fetchRaces(): List<Race> = fetchModel(RACES_URL)

suspend fun fetchFeats(): List<Feat> = fetchModel(FEATS_URL)

fun storeCharacter(character: Character) = MainScope().launch {
    val headers = Headers()
    headers.append(CONTENT_TYPE, TYPE_JSON)
    val request = RequestInit(method = METHOD_POST, headers = headers, body = Json.encodeToString(character))
    window.fetch(CHARACTERS_URL, request).await().apply { window.location.reload() }
}

fun imageFromEvent(event: Event, callback: (Event) -> Unit) {
    val file = (event.target as HTMLInputElement).files?.get(0) ?: Blob()
    val reader = FileReader()
    reader.onloadend = callback
    reader.readAsDataURL(file)
}

fun RBuilder.loadRaces(
    setRaces: (Map<String, Race>) -> Unit
) {
    circularProgress()
    MainScope().launch {
        setRaces(fetchRaces().associateBy { it._id })
    }
}

fun RBuilder.loadFeats(
    setFeats: (Map<String, Feat>) -> Unit
) {
    circularProgress()
    MainScope().launch {
        setFeats(fetchFeats().associateBy { it._id })
    }
}

private suspend inline fun <reified T> fetchModel(url: String): T =
    Json.decodeFromString(window.fetch(url).await().text().await())

class FeatureWindowSettings(
    val open: Boolean,
    val setOpen: StateSetter<Boolean>,
    val feature: Feature,
    val setFeature: StateSetter<Feature>,
    val function: DnDFunction,
    val setFunction: StateSetter<DnDFunction>
) {
    fun setParams(open: Boolean, feature: Feature, function: DnDFunction) {
        this.setOpen(open)
        this.setFeature(feature)
        this.setFunction(function)
    }
}

fun useFeatureWindowSettings(): FeatureWindowSettings {
    val (open, setOpen) = useState(false)
    val (feature, setFeature) = useState(Feature("", ""))
    val (function, setFunction) = useState(DnDFunction(""))

    return FeatureWindowSettings(open, setOpen, feature, setFeature, function, setFunction)
}
