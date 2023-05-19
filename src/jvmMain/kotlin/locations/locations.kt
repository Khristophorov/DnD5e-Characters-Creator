@file:OptIn(KtorExperimentalLocationsAPI::class)

package me.khrys.dnd.charcreator.server.locations

import io.ktor.server.locations.KtorExperimentalLocationsAPI
import io.ktor.server.locations.Location
import me.khrys.dnd.charcreator.common.CHARACTERS_URL
import me.khrys.dnd.charcreator.common.FEATS_URL
import me.khrys.dnd.charcreator.common.IMAGE_URL
import me.khrys.dnd.charcreator.common.LOGIN_URL
import me.khrys.dnd.charcreator.common.LOGOUT_URL
import me.khrys.dnd.charcreator.common.MANEUVERS_URL
import me.khrys.dnd.charcreator.common.RACES_URL
import me.khrys.dnd.charcreator.common.ROOT_URL
import me.khrys.dnd.charcreator.common.SPELLS_URL
import me.khrys.dnd.charcreator.common.TRANSLATIONS_URL

@Location(ROOT_URL)
class Index

@Location(LOGIN_URL)
class Login

@Location(LOGOUT_URL)
class Logout

@Location(TRANSLATIONS_URL)
class Translations

@Location(CHARACTERS_URL)
class Characters

@Location(RACES_URL)
class Races

@Location(FEATS_URL)
class Feats

@Location(MANEUVERS_URL)
class Maneuvers

@Location(SPELLS_URL)
class Spells

@Location("${IMAGE_URL}/{name}")
data class Image(val name: String)
