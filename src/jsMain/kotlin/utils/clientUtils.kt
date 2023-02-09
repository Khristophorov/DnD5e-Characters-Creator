package me.khrys.dnd.charcreator.client.utils

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import me.khrys.dnd.charcreator.common.CHARACTERS_URL
import me.khrys.dnd.charcreator.common.FEATS_URL
import me.khrys.dnd.charcreator.common.MANEUVERS_URL
import me.khrys.dnd.charcreator.common.RACES_URL
import me.khrys.dnd.charcreator.common.TRANSLATIONS_URL
import me.khrys.dnd.charcreator.common.models.Character
import me.khrys.dnd.charcreator.common.models.Feat
import me.khrys.dnd.charcreator.common.models.Maneuver
import me.khrys.dnd.charcreator.common.models.Race
import me.khrys.dnd.charcreator.common.models.Translation

private val client = HttpClient {
    install(ContentNegotiation) {
        json()
    }
}

fun loadTranslations(setTranslations: (Map<String, String>) -> Unit) {
    console.info("Loading translations.")
    MainScope().launch {
        setTranslations(fetchTranslations().associate { it._id to it.value })
    }
}

fun loadCharacters(setCharacters: (List<Character>) -> Unit) {
    console.info("Loading characters.")
    MainScope().launch {
        setCharacters(fetchCharacters())
    }
}

fun loadRaces(setRaces: (Map<String, Race>) -> Unit) {
    console.info("Loading races.")
    MainScope().launch {
        setRaces(fetchRaces().associateBy { it._id })
    }
}

fun loadFeats(setFeats: (Map<String, Feat>) -> Unit) {
    console.info("Loading feats.")
    MainScope().launch {
        setFeats(fetchFeats().associateBy { it._id })
    }
}

fun loadManeuvers(setManeuvers: (Map<String, Maneuver>) -> Unit) {
    console.info("Loading maneuvers.")
    MainScope().launch {
        setManeuvers(fetchManeuvers().associateBy { it._id })
    }
}

fun storeCharacter(character: Character) = MainScope().launch {
    client.post(CHARACTERS_URL) {
        contentType(ContentType.Application.Json)
        setBody(character)
    }
    window.location.reload()
}

private suspend fun fetchTranslations(): List<Translation> = fetchModel(TRANSLATIONS_URL)

private suspend fun fetchCharacters(): List<Character> = fetchModel(CHARACTERS_URL)

private suspend fun fetchRaces(): List<Race> = fetchModel(RACES_URL)

private suspend fun fetchFeats(): List<Feat> = fetchModel(FEATS_URL)

private suspend fun fetchManeuvers(): List<Maneuver> = fetchModel(MANEUVERS_URL)

private suspend inline fun <reified T> fetchModel(url: String): T =
    client.get(url).body()
