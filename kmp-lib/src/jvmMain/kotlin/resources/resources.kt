package me.khrys.dnd.charcreator.server.resources

import io.ktor.resources.Resource
import me.khrys.dnd.charcreator.common.CHARACTERS_URL
import me.khrys.dnd.charcreator.common.CLASSES_URL
import me.khrys.dnd.charcreator.common.FEATS_URL
import me.khrys.dnd.charcreator.common.IMAGE_URL
import me.khrys.dnd.charcreator.common.LOGIN_URL
import me.khrys.dnd.charcreator.common.LOGOUT_URL
import me.khrys.dnd.charcreator.common.MANEUVERS_URL
import me.khrys.dnd.charcreator.common.RACES_URL
import me.khrys.dnd.charcreator.common.ROOT_URL
import me.khrys.dnd.charcreator.common.SPELLS_URL
import me.khrys.dnd.charcreator.common.TRANSLATIONS_URL

@Resource(ROOT_URL)
class Index

@Resource(LOGIN_URL)
class Login

@Resource(LOGOUT_URL)
class Logout

@Resource(TRANSLATIONS_URL)
class Translations

@Resource(CHARACTERS_URL)
class Characters

@Resource(RACES_URL)
class Races

@Resource(CLASSES_URL)
class Classes

@Resource(FEATS_URL)
class Feats

@Resource(MANEUVERS_URL)
class Maneuvers

@Resource(SPELLS_URL)
class Spells

@Resource("${IMAGE_URL}/{name}")
data class Image(val name: String)
