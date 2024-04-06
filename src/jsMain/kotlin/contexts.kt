package me.khrys.dnd.charcreator.client

import me.khrys.dnd.charcreator.common.models.Character
import me.khrys.dnd.charcreator.common.models.Feat
import me.khrys.dnd.charcreator.common.models.Maneuver
import me.khrys.dnd.charcreator.common.models.Race
import me.khrys.dnd.charcreator.common.models.Spell
import react.Context
import react.createContext

val TranslationsContext: Context<Map<String, String>> = createContext(emptyMap())

val CharactersContext: Context<List<Character>> = createContext(emptyList())

val FeatsContext: Context<Map<String, Feat>> = createContext(emptyMap())

var RacesContext: Context<List<Race>> = createContext(emptyList())

val ManeuversContext: Context<Map<String, Maneuver>> = createContext(emptyMap())

val SpellsContext: Context<Map<String, Spell>> = createContext(emptyMap())
